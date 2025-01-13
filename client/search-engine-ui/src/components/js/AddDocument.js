import React, { useState } from 'react';
import axios from 'axios';
import '../css/UploadDocument.css';

const AddDocument  =  () => {
    const [file,setFile] = useState(null);
    const [message, setMessage] = useState('');

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    }

    const handleSubmit = async(e) => {
        e.preventDefault();
        const formData = new FormData();
        formData.append('file',file);

        try {
            const response = await axios.post('http://localhost:8080/api/files/upload',formData,{
                headers: {
                    'Content-Type' : 'multipart/form-data',
                }
            })
            setMessage('File Uploaded Successfully');
        }
        catch(error) {
            console.error("Error uploading the file",error);
            setMessage('Failed to upload the file');
        }

    }

    return(
        <div className='upload-document-container'>
            <h2>Upload a document</h2>
            <form onSubmit={handleSubmit}>
                <div className="input-group">
                    <label htmlFor="fileInput">Select File:</label>
                    <input type="file" id="fileInput" onChange={handleFileChange}></input>
                </div>
                <button type="submit" className="upload-button">Upload</button>
            </form>
            {message && <p className="message">{message}</p>}
        </div>
    )
}

export default AddDocument;