import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../styles/Login.css";

const Login = () => {
  const navigate=useNavigate();

  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try{
      const response=await fetch("http://localhost:8080/api/auth/login",{
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email:formData.email,
          password: formData.password,
        }),
      });
      const token=await response.text();
      localStorage.setItem("token",token);
      
      if(response.ok){
        navigate("/dashboard");
      } else{
        alert("Login failed");
      }
    } catch(error){
      console.log("Error:",error);
      alert("Something went wrong");
    }

  };

  return (
    <div className="login-page">
      <div className="login-card">
        <h2>Welcome Back</h2>
        <p>Login to continue to Resume Analyzer</p>

        <form onSubmit={handleSubmit}>
          <label>Email</label>
          <input
            type="email"
            name="email"
            placeholder="Enter your email"
            value={formData.email}
            onChange={handleChange}
            required
          />

          <label>Password</label>
          <input
            type="password"
            name="password"
            placeholder="Enter your password"
            value={formData.password}
            onChange={handleChange}
            required
          />

          <button type="submit">Login</button>
        </form>

        <span>
          Don&apos;t have an account? <Link to="/register">Register</Link>
        </span>
      </div>
    </div>
  );
};

export default Login;