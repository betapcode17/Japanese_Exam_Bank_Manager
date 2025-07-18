# Japanese Exam Bank Management System with AI Integration

## 📘 Overview

This project is a **Java-based desktop application** designed to manage a question bank for Japanese language exams. It integrates **AI technologies** to assist with question entry and answer suggestions, making the process faster and more accurate. The system supports audio-based questions, manual data management, and exporting fully formatted test papers with answer keys.

---

## 🚀 Key Features

- **🧠 AI Integration**
  - Supports AI-powered suggestions for answers and even auto-generation of questions (can use external libraries or APIs).
  
- **📝 Exam Management**
  - Add, edit, and delete exam questions manually.
  - Support for multiple-choice, fill-in-the-blank, and listening-based questions.
  
- **🔊 Audio Handling**
  - If a question includes audio, the file is saved to the local disk and its path stored in the database.
  
- **🗃️ Database Integration**
  - All questions, answers, and metadata are stored in a relational database (e.g., MySQL, SQLite).
  
- **📤 Export Functionality**
  - Automatically generate randomized test papers (multiple versions) from the question bank.
  - Export to PDF or DOC formats.
  - Each test comes with a separate answer key file.

---

## 🛠️ Technologies Used

- **Java Swing** – for GUI development  
- **JDBC** – for database connectivity  
- **MySQL/SQLite** – for storing questions and audio file paths  
- **Apache POI / iText** – for exporting to DOC/PDF  
- **External AI Libraries / APIs** – for question/answer suggestion (e.g., OpenAI, HuggingFace)

---



