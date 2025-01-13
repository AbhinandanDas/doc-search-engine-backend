import React, { useState,useEffect,useRef } from 'react';
import '../css/SearchComponent.css';
import { getSuggestions, searchLogic } from '../../api/searchApi';

function SearchComponent() {
    const [searchTerm, setSearchTerm] = useState('');
    const [results, setResults] = useState([]);
    const [suggestions,setSuggestions] = useState([]);
    const [recentSearches,setRecentSearches] = useState([]);
    const [showRecentSearches,setShowRecentSearches] = useState(false); // To control to recent searches visibility.

    const searchInputRef = useRef(null);

    useEffect(() => {
        // fetching recent searches from the local storage.
       const fetchSearches = JSON.parse(localStorage.getItem('recentSearches')) || [];
       setRecentSearches(fetchSearches);
    },[]);

    useEffect(() => {
        if(searchTerm) {
            const fetchSuggestions = async () => {
                try {
                    const response = await getSuggestions(searchTerm);
                    setSuggestions(response.data);
                }
                catch(error){
                    console.error("Error fetching suggestions",error);
                }
            };
            fetchSuggestions();
        }
        else{
            setSuggestions([]);
        }
    },[searchTerm]);

    const handleSearch = async (e) => {
        e.preventDefault();
        try {
            const response = await searchLogic(searchTerm);
            setResults(response.data);

            //save the search result into local storage.
            saveRecentSearch(searchTerm);
        }
        catch(error) {
            console.error("Could not find search word",error);
            setResults([]);
        }
    }

    const saveRecentSearch = (term) => {
        const fetchSearches = JSON.parse(localStorage.getItem('recentSearches')) || [];

        // We can save upto 5 recent search terms.
        if(!fetchSearches.includes(term)) {
            const updatedSearches = [term,...fetchSearches.slice(0,4)];
            // Saving to local storage
            localStorage.setItem('recentSearches',JSON.stringify(updatedSearches));
            setRecentSearches(updatedSearches);
        }
    }

    const handleFocus = () => {
        setShowRecentSearches(true);
    }

    const handleClickOutside = (e) => {
        if(searchInputRef.current && !searchInputRef.current.contains(e.target)) {
            setShowRecentSearches(false);
        }
    }

    useEffect(() => {
        document.addEventListener('mousedown',handleClickOutside);
        return () => {
            document.removeEventListener('mousedown',handleClickOutside);
        };
    },[]);


    return (
        <div className='search-container' ref={searchInputRef}>
            <h2>Search Docs</h2>

            {/* Search Area*/}
            <input type="text" value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} onFocus={handleFocus} 
            placeholder="Enter the search term" required/>

            {/*Show recent searches,
            if my search term state is empty  and if user has some recent searches and whether user has focused on the search box*/}
            {showRecentSearches && searchTerm==='' && recentSearches.length > 0 && (
                <div className="suggestion-box">
                    <ul className="suggestions-list">
                        {recentSearches.map((recentSearch,index) => (
                           <li key={index} onClick={() =>{ setSearchTerm(recentSearch); setShowRecentSearches(false)}} >
                                {recentSearch}
                           </li> 
                        ))}
                    </ul>
                </div>
            )}


            {/*Suggestions based on user input */}
            {suggestions.length > 0 && (
                <div className="suggestion-box">
                    <ul className="suggestions-list">
                        {suggestions.map((suggestion,index) => (
                            <li key={index} onClick={() => {setSearchTerm(suggestion); setSuggestions([])} }>
                                {suggestion}
                            </li>
                        ))}
                    </ul>
                </div>
                
            )}
            <button onClick={handleSearch}>Search</button> 
            {/* Error to be display in case of an error
            {error && <p>{error}</p>} */}

            {results.length > 0 && (
                <div className="search-results">
                    <h2>Results:</h2>
                    <ul>
                        {results.map((fileName,index) => (
                           <li key={index}>
                            <a href={`http://localhost:8080/api/files/${fileName}`} download>
                            {fileName}
                            </a>
                           </li> 
                        ))}
                    </ul>
                </div>
            )}

            {results.length === 0 && (
                <div className="search-results">
                    <h3>No results</h3>
                </div>
            )}
        </div>
    )
}

export default SearchComponent;
