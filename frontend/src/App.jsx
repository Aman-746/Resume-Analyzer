import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Register from "./pages/Register";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import ProtectedRoute from "./components/ProtectedRoute";
import PublicRoute from "./components/PublicRoute";
import Analysis from "./pages/Analysis";

function App() {
  return(
    <BrowserRouter>
    <Routes>
      <Route path="/" element={<Navigate to="/login"/>}/>
      <Route path="/login" element={
        <PublicRoute>
          <Login/>
        </PublicRoute>
      }/>
      <Route path="/register" element={
        <PublicRoute>
          <Register/>
        </PublicRoute>
      }/>
      <Route path="/dashboard" element={
        <ProtectedRoute>
          <Dashboard/>
        </ProtectedRoute>
      }/>
      <Route path="/resume/:resumeId/analysis" element={
        <ProtectedRoute>
          <Analysis/>
        </ProtectedRoute>
      }/>
    </Routes>
  </BrowserRouter>
  );
}

export default App;