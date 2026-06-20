from flask import Flask, request, jsonify
from dotenv import load_dotenv
import google.generativeai as genai
import pdfplumber
import json
import os

app=Flask(__name__)

# ==========================
# Gemini Configuration
# ==========================

load_dotenv()
genai.configure(
    api_key=os.getenv("GEMINI_API_KEY")
)

# Check what are all the models available for your API version or account
# for model in genai.list_models():
#     print(model.name)

model=genai.GenerativeModel("gemini-2.5-flash")

# ==========================
# Prompt Builder
# ==========================

def build_prompt(resume_text):
    return f"""
    You are an expert ATS and Resume Reviewer.
    Analyze the following resume.

    IMPORTANT:
    - Return ONLY valid JSON.
    - Never return null.
    - strengths, weaknesses, and suggestions must always be arrays.
    - If no weakness is found, provide at least one improvement opportunity.
    - ATS score must be between 0 and 100.
    
    JSON format:

    {{
    "atsScore":0,
    "strengths":[],
    "weaknesses":[],
    "suggestions":[],
    "summary":""
    }}

    Resume:

    {resume_text}
    """

# ==========================
# Analyze Resume API
# ==========================


@app.route("/")
def home():
    # JUST TO CHECK IF .env IS BEING READ CORRECTLY
    # load_dotenv();
    # print(os.getenv("GEMINI_API_KEY"))
    return "AI Service Running"

@app.route("/analyze", methods=["POST"])
def analyze_resume():
    try:

        data=request.get_json()
        file_path=data.get("filePath")
        extracted_text=""
        with pdfplumber.open(file_path) as pdf:
            for page in pdf.pages:
                text=page.extract_text()
                if text:
                    extracted_text += text+"\n"

        print("Text extraction completed")

        prompt=build_prompt(extracted_text)

        response=model.generate_content(prompt)

        # print(response.text)

        result_text=response.text

        cleaned_response = (
            result_text
            .replace("```json","")
            .replace("```","")
            .strip()
        )

        analysis_json = json.loads(cleaned_response)
        return jsonify(analysis_json)
    
    except Exception as e:
        print("Error", str(e))
        return jsonify({
            "error":str(e)
        }), 500

# ==========================
# Start Application
# ==========================

if __name__=="__main__":
    app.run(debug=True, port=5000)