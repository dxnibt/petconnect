import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import RegisterPage from "./Auth/pages/RegisterPage";
import RefugioPage from "./Auth/pages/RefugioPage.jsx";
import AdoptantePage from "./Auth/pages/AdoptantePage.jsx";
import HomePage from "./pets/pages/HomePage.jsx";


function App() {
  return (
    <Router>
      <Routes>
        <Route path="/refugio" element={<RefugioPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/adoptante" element={<AdoptantePage />} />
        <Route path="/" element={<HomePage/>} />
      </Routes>
    </Router>
  );
}

export default App;
