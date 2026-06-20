import { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import "../styles/Analysis.css";

const Analysis = () => {
  const { resumeId } = useParams();
  const navigate = useNavigate();
  const location = useLocation();

  const token = localStorage.getItem("token");

  const analysisStatus = location.state?.analysisStatus;

  const [analysis, setAnalysis] = useState(null);
  const [loading, setLoading] = useState(true);
  const [analyzing, setAnalyzing] = useState(false);

  const fetchAnalysis = async () => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/resumes/${resumeId}/analysis`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );

      const result = await response.json();

      if (response.ok) {
        setAnalysis(result);
      } else {
        alert("Failed to load analysis");
      }
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const loadAnalysis = async () => {
      if (analysisStatus === "ANALYZED") {
        await fetchAnalysis();
      } else {
        setLoading(false);
      }
    };

    loadAnalysis();
  }, []);

  const analyzeResume = async () => {
    try {
      setAnalyzing(true);

      const response = await fetch(
        `http://localhost:8080/api/resumes/${resumeId}/analyze`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );

      const result = await response.json();

      if (response.ok) {
        setAnalysis(result);
      } else {
        alert("Analysis failed");
      }
    } catch (error) {
      console.error("Error in analysis:", error);
    } finally {
      setAnalyzing(false);
    }
  };

  if (loading) {
    return (
      <div className="analysis-page">
        <p>Loading analysis...</p>
      </div>
    );
  }

  return (
    <div className="analysis-page">
      <header className="analysis-header">
        <div>
          <h2>Resume Analysis</h2>
          <p>Resume ID: {resumeId}</p>
        </div>

        <button
          className="secondary-btn"
          onClick={() => navigate("/dashboard")}
        >
          Back to Dashboard
        </button>
      </header>

      <main className="analysis-content">
        {!analysis ? (
          <section className="analysis-card">
            <h3>Resume Not Analyzed Yet</h3>

            <p>Generate AI-powered feedback for this resume.</p>

            <button
              onClick={analyzeResume}
              className="primary-btn"
              disabled={analyzing}
            >
              {analyzing ? "Analyzing..." : "Analyze Resume"}
            </button>
          </section>
        ) : (
          <>
            <section className="score-card">
              <h3>ATS Score</h3>
              <h1>{analysis.atsScore}/100</h1>

              <div className="score-label">
                {analysis.atsScore >= 85
                  ? "Excellent"
                  : analysis.atsScore >= 70
                    ? "Good"
                    : analysis.atsScore >= 50
                      ? "Average"
                      : "Needs Improvement"}
              </div>
            </section>

            <section className="analysis-grid">
              <div className="info-card">
                <h4>Strengths</h4>

                <div className="point-list">
                  {analysis.strengths?.map((item, index) => (
                    <div className="point-item strength" key={index}>
                      ✅ {item}
                    </div>
                  ))}
                </div>
              </div>

              <div className="info-card">
                <h4>Weaknesses</h4>

                <div className="point-list">
                  {analysis.weaknesses?.map((item, index) => (
                    <div className="point-item weakness" key={index}>
                      ⚠️ {item}
                    </div>
                  ))}
                </div>
              </div>

              <div className="info-card">
                <h4>Suggestions</h4>

                <div className="point-list">
                  {analysis.suggestions?.map((item, index) => (
                    <div className="point-item suggestion" key={index}>
                      💡 {item}
                    </div>
                  ))}
                </div>
              </div>
            </section>

            <section className="summary-card">
              <h3>AI Summary</h3>
              <p>{analysis.summary}</p>
            </section>
          </>
        )}
      </main>
    </div>
  );
};

export default Analysis;
