<p align="center">
  <img src="https://readme-typing-svg.herokuapp.com/?font=Fira+Code&size=34&duration=4000&pause=1000&color=3B82F6&center=true&vCenter=true&width=900&lines=Resume+Analyzer;AI-Powered+ATS+Resume+Reviewer;Gemini+%B7+Spring+Boot+%B7+React+%B7+MySQL" alt="Typing SVG" />
</p>

<p align="center">
  <a href="https://github.com/Aman-746/Resume-Analyzer/stargazers">
    <img src="https://img.shields.io/github/stars/Aman-746/Resume-Analyzer?style=for-the-badge&logo=github&color=FFD700&labelColor=111827" alt="Stars"/>
  </a>
  <a href="https://github.com/Aman-746/Resume-Analyzer/network/members">
    <img src="https://img.shields.io/github/forks/Aman-746/Resume-Analyzer?style=for-the-badge&logo=github&color=0E75B6&labelColor=111827" alt="Forks"/>
  </a>
  <a href="https://github.com/Aman-746/Resume-Analyzer/issues">
    <img src="https://img.shields.io/github/issues/Aman-746/Resume-Analyzer?style=for-the-badge&logo=github&color=EF4444&labelColor=111827" alt="Issues"/>
  </a>
  <a href="https://github.com/Aman-746/Resume-Analyzer/commits/main">
    <img src="https://img.shields.io/github/last-commit/Aman-746/Resume-Analyzer?style=for-the-badge&logo=github&color=10B981&labelColor=111827" alt="Last Commit"/>
  </a>
</p>

<p align="center">
  <a href="https://www.linkedin.com/in/amaninprod/" target="_blank">
    <img src="https://img.shields.io/badge/LinkedIn-aman--kumar--41090a200-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white" alt="LinkedIn"/>
  </a>
  <a href="mailto:aman04012000@gmail.com">
    <img src="https://img.shields.io/badge/Email-aman04012000@gmail.com-EA4335?style=for-the-badge&logo=gmail&logoColor=white" alt="Email"/>
  </a>
</p>

---

## 📖 Overview

**Resume Analyzer** is an AI-powered ATS (Applicant Tracking System) resume reviewer that analyzes uploaded resumes using Google Gemini. It provides an ATS score, identifies strengths and weaknesses, and offers actionable suggestions to improve your resume for job applications.

> 🎯 **Built for:** Job seekers who want to optimize their resumes for ATS systems and improve their chances of getting shortlisted.

---

## ✨ Features

| Feature | Description |
|---------|-------------|
| 📄 **PDF Resume Upload** | Upload and parse PDF resumes using pdfplumber |
| 🤖 **AI Analysis** | Google Gemini-powered resume analysis |
| 📊 **ATS Score** | Get an ATS compatibility score (0-100) |
| 💪 **Strengths & Weaknesses** | AI-identified strengths and areas for improvement |
| 💡 **Actionable Suggestions** | Specific recommendations to improve your resume |
| 🔐 **Spring Boot Backend** | Secure REST API with Java & Spring Boot |
| ⚛� **React Frontend** | Modern React 19 + Vite frontend |
| 🗄️ **MySQL Database** | Persistent storage for analysis results |

---

## 🛠️ Tech Stack

| Layer | Technologies |
|-------|-------------|
| **Frontend** | <a href="https://reactjs.org/" target="_blank"><img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/react/react-original-wordmark.svg" width="24" height="24"/></a> React 19 · <a href="https://vitejs.dev/" target="_blank"><img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/vitejs/vitejs-original.svg" width="24" height="24"/></a> Vite · React Router |
| **Backend** | <a href="https://spring.io/projects/spring-boot" target="_blank"><img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original-wordmark.svg" width="24" height="24"/></a> Spring Boot · <a href="https://www.mysql.com/" target="_blank"><img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original-wordmark.svg" width="24" height="24"/></a> MySQL · Maven · Java |
| **AI Service** | <a href="https://flask.palletsprojects.com/" target="_blank"><img src="https://cdn.simpleicons.org/flask/000000" width="24" height="24"/></a> Flask · <a href="https://python.org" target="_blank"><img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/python/python-original.svg" width="24" height="24"/></a> Python · <a href="https://ai.google.dev/" target="_blank"><img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/google/google-original.svg" width="24" height="24"/></a> Google Gemini · pdfplumber |

---

## 📁 Project Structure

```
Resume-Analyzer/
├── backend/                    # Spring Boot backend
│   ├── src/main/java/com/resumeanalyzer/backend/
│   ├── pom.xml
│   └── application.properties
├── ai-service/                 # Python Flask AI service
│   ├── app.py                  # Flask entry point
│   └── requirements.txt
├── frontend/                   # React frontend
│   ├── src/
│   ├── package.json
│   └── vite.config.js
└── README.md
```

---

## 🚀 Quick Start

### Prerequisites

- Java 17+
- Maven
- Node.js 18+
- Python 3.9+
- MySQL 8.0+

### 1. Clone the Repository

```bash
git clone https://github.com/Aman-746/Resume-Analyzer.git
cd Resume-Analyzer
```

### 2. Database Setup

```sql
CREATE DATABASE resume_analyzer;
```

### 3. Backend Configuration

Update `backend/src/main/resources/application.properties`:

```properties
spring.application.name=resume-analyzer-backend
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/resume_analyzer
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 4. AI Service Setup

```bash
cd ai-service
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

Create `ai-service/.env`:

```env
GEMINI_API_KEY=YOUR_GEMINI_API_KEY
```

### 5. Start Backend

```bash
cd backend
mvn spring-boot:run
# http://localhost:8080
```

### 6. Start AI Service

```bash
cd ai-service
python app.py
# http://localhost:5000
```

### 7. Start Frontend

```bash
cd frontend
npm install
npm run dev
# http://localhost:5173
```

---

## 🔧 API Endpoints

### Resume Analysis
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/analyze` | Upload and analyze a resume |

### Response Format
```json
{
  "atsScore": 85,
  "strengths": ["Clear formatting", "Relevant skills"],
  "weaknesses": ["Missing quantifiable achievements"],
  "suggestions": ["Add metrics to project descriptions"],
  "summary": "Strong technical resume with room for improvement"
}
```

---

## 📸 Screenshots

<table>
  <tr>
    <td><b>Upload Page</b></td>
    <td><b>Analysis Results</b></td>
  </tr>
  <tr>
    <td>Upload your PDF resume</td>
    <td>ATS score and AI suggestions</td>
  </tr>
</table>

---

## 🔮 Future Enhancements

- [ ] Support for DOCX and TXT resume formats
- [ ] Resume builder with templates
- [ ] Job description matching score
- [ ] User authentication and history
- [ ] Export analysis as PDF
- [ ] Docker containerization
- [ ] Cloud deployment

---

## 🤝 Contributing

Contributions are welcome! Please open an issue or submit a pull request.

---

## 📞 Contact

**Aman Kumar** — Full Stack Developer | AI Engineer

📧 aman04012000@gmail.com &nbsp;•&nbsp; 📱 +91-7462933412 &nbsp;•&nbsp; 🇮🇳 India  
🔗 [LinkedIn](https://www.linkedin.com/in/amaninprod/) &nbsp;•&nbsp; [GitHub](https://github.com/Aman-746)

---

<p align="center">
  <img src="https://komarev.com/ghpvc/?username=Aman-746&repo=Resume-Analyzer&label=Repo%20views&color=0e75b6&style=flat-square&start=2000" alt="Repo views"/>
</p>
