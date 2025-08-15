# 📄 Document Search Engine

A high-performance document search engine built with **Java Spring Boot** that allows you to upload, store, and search across text, DOCX, and PDF files with **TF-IDF ranking**, **stemming**, and **synonym support**.  
Uploaded files are stored securely in **Amazon S3**, and indexed content is saved in **PostgreSQL** for lightning-fast retrieval.

---

## 🚀 Features

- **Multi-format Uploads** — Supports `.txt`, `.docx`, and `.pdf` files.
- **Amazon S3 Integration** — Secure cloud storage for all uploaded files.
- **PostgreSQL Database** — Stores extracted text and search metadata.
- **TF-IDF Based Ranking** — Returns the most relevant files first.
- **Stemming & Synonym Support** — Improves search recall and accuracy.
- **Direct File Links** — Easily open the matched documents from search results.

---

## 🛠 Tech Stack

| Layer              | Technology |
|--------------------|------------|
| **Backend**        | Java 17, Spring Boot |
| **Database**       | PostgreSQL |
| **Cloud Storage**  | Amazon S3 |
| **Text Processing**| Apache POI (DOCX), Apache PDFBox (PDF), Custom Stemming Service |
| **Search Ranking** | TF-IDF Algorithm |
| **Build Tool**     | Maven |

---

## 🏗 Architecture Overview

The system is composed of:
1. **Upload Service** — Handles file uploads, stores them in S3, and extracts text.
2. **Indexing Service** — Processes text, applies stemming & synonyms, and stores it in PostgreSQL.
3. **Search Service** — Runs TF-IDF ranking to return the most relevant matches.
4. **API Layer** — Exposes REST endpoints for file upload & search.
5. **Client UI (Optional)** — A minimal interface to interact with the API.


---

## ⚙️ Installation & Setup

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/your-username/document-search-engine.git
cd document-search-engine
