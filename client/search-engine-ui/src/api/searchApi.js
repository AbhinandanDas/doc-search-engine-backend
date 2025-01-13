import axios from 'axios'

const BASE_URL = 'http://localhost:8080/api/search'

export const searchLogic = (term) => {
    return axios.get(`${BASE_URL}/term?term=${term}`);
};

export const addDocument = (docId,content) => {
    return axios.post(`${BASE_URL}/add`,{docId,content});
}

export const getSuggestions = (searchTerm) => {
    return axios.get(`http://localhost:8080/api/search/suggestions?term=${searchTerm}`);
}