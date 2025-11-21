import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import RegisterPage from "./Auth/pages/RegisterPage.jsx";
import RefugioPage from "./Auth/pages/RefugioPage.jsx";
import AdoptantePage from "./Auth/pages/AdoptantePage.jsx";
import HomePage from "./pets/pages/HomePage.jsx";
import PetDetails from "./pets/pages/PetDetails.jsx"
import PetForm from "./pets/pages/PetForm.jsx"
import PetList from "./pets/pages/PetList.jsx"


function App() {
  return (
    <Router>
      <Routes>
        <Route path="/refugio" element={<RefugioPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/adoptante" element={<AdoptantePage />} />
        <Route path="/editar" element={<PetDetails/>} />
        <Route path="/ver" element={<PetList/>} />
        <Route path= "crear" element={<PetForm/>}/>
        <Route path="/" element={<HomePage/>} />
        
      </Routes>
    </Router>
  );
}

export default App;
