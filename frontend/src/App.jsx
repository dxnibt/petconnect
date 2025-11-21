import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import HomePage from "./pets/pages/HomePage.jsx";
import AuthCombined from "./Auth/pages/AuthCombined.jsx";
import RefugioPage from "./Auth/pages/RefugioPage.jsx";
import AdoptantePage from "./Auth/pages/AdoptantePage.jsx";

function App() {
  return (
    <Router>
      <Routes>
        {/* Home */}
        <Route path="/" element={<HomePage />} />

        {/* Autenticación */}
        <Route path="/register" element={<AuthCombined />} />

        {/* Roles */}
        <Route path="/refugio" element={<RefugioPage />} />
        <Route path="/adoptante" element={<AdoptantePage />} />
      </Routes>
    </Router>
  );
}

export default App;