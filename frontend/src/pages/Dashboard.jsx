import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/Dashboard.css";

const Dashboard = () => {
  const navigate = useNavigate();

  const [selectedFile, setSelectedFile] = useState(null);
  const [uploading, setUploading] = useState(false);

  const [resumes, setResumes] = useState([]);
  const [loading, setLoading] = useState(true);

  const [stats, setStats] = useState({
    totalResumes: 0,
    analysesDone: 0,
    bestScore: 0,
  });

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];

    if (!file) return;

    const allowedTypes = [
      "application/pdf",
      "application/msword",
      "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
    ];

    if (!allowedTypes.includes(file.type)) {
      alert("Only PDF, DOC, and DOCX files are allowed");
      return;
    }

    setSelectedFile(file);
  };

  const handleUpload = async () => {
    if (!selectedFile) {
      alert("Please select a resume first");
      return;
    }

    const token = localStorage.getItem("token");

    const formData = new FormData();
    formData.append("resume", selectedFile);

    try {
      setUploading(true);

      const response = await fetch(
        "http://localhost:8080/api/resumes/upload",
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
          },
          body: formData,
        }
      );

      const result = await response.json();

      if (response.ok) {
        alert(result.status);
        setSelectedFile(null);

        navigate(`/resume/${result.resumeId}/analysis`);
      } else {
        alert(result.message || "Resume upload failed");
      }
    } catch (error) {
      console.error("Upload error:", error);
      alert("Something went wrong while uploading");
    } finally {
      setUploading(false);
    }
  };

  const fetchResumes = async () => {
    try {
      const token = localStorage.getItem("token");

      const response = await fetch(
        "http://localhost:8080/api/resumes",
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      const data = await response.json();

      if (response.ok) {
        setResumes(data);
      } else {
        alert("Failed to fetch resumes");
      }
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const fetchDashboardStats = async () => {
    try {
      const token = localStorage.getItem("token");

      const response = await fetch(
        "http://localhost:8080/api/resumes/dashboard/stats",
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      const data = await response.json();

      if (response.ok) {
        setStats(data);
      } else {
        console.error("Failed to fetch dashboard stats");
      }
    } catch (error) {
      console.error("Stats fetch error:", error);
    }
  };

  useEffect(() => {
    const loadDashboard = async () => {
      await Promise.all([
        fetchResumes(),
        fetchDashboardStats(),
      ]);
    };

    loadDashboard();
  }, []);

  return (
    <div className="dashboard-page">
      <header className="dashboard-header">
        <div>
          <h2>Resume Analyzer</h2>
          <p>Welcome back 👋</p>
        </div>

        <button onClick={handleLogout} className="logout-btn">
          Logout
        </button>
      </header>

      <main className="dashboard-content">
        <section className="stats-grid">
          <div className="stat-card">
            <h3>{stats.totalResumes}</h3>
            <p>Total Resumes</p>
          </div>

          <div className="stat-card">
            <h3>{stats.analysesDone}</h3>
            <p>Analyses Done</p>
          </div>

          <div className="stat-card">
            <h3>{stats.bestScore || "-"}</h3>
            <p>Best Score</p>
          </div>
        </section>

        <section className="action-card">
          <h3>Upload Your Resume</h3>

          <p>
            Upload your resume and get AI-powered suggestions
            to improve it.
          </p>

          <div className="upload-box">
            <input
              type="file"
              accept=".pdf,.doc,.docx"
              onChange={handleFileChange}
            />

            {selectedFile && (
              <p className="selected-file">
                Selected: <strong>{selectedFile.name}</strong>
              </p>
            )}

            <button
              className="primary-btn"
              onClick={handleUpload}
              disabled={uploading}
            >
              {uploading
                ? "Uploading..."
                : "Upload Resume"}
            </button>
          </div>
        </section>

        <section className="resume-section">
          <h3>Your Resumes</h3>

          {loading ? (
            <p>Loading resumes...</p>
          ) : resumes.length === 0 ? (
            <div className="empty-state">
              <p>No resumes uploaded yet.</p>
              <span>
                Upload your first resume to get started.
              </span>
            </div>
          ) : (
            <div className="resume-list">
              {resumes.map((resume) => (
                <div
                  className="resume-card"
                  key={resume.id}
                >
                  <div>
                    <h4>{resume.originalFileName}</h4>

                    <p>
                      {(resume.fileSize / 1024).toFixed(2)} KB
                    </p>

                    <span>
                      Uploaded on{" "}
                      {new Date(
                        resume.uploadedAt
                      ).toLocaleDateString()}
                    </span>
                  </div>

                  <div className="resume-actions">
                    <button
                      className="primary-btn"
                      onClick={() =>
                        navigate(
                          `/resume/${resume.id}/analysis`,
                          {
                            state: {
                              analysisStatus:
                                resume.analysisStatus,
                            },
                          }
                        )
                      }
                    >
                      View Analysis
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </section>
      </main>
    </div>
  );
};

export default Dashboard;