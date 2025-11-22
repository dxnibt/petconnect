import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./hooks/useAuth.jsx"; // Asegúrate de que la ruta sea correcta
import RegisterPage from "./Auth/pages/RegisterPage.jsx";
import RefugioPage from "./Auth/pages/RefugioPage.jsx";
import AdoptantePage from "./Auth/pages/AdoptantePage.jsx";
import HomePage from "./pets/pages/HomePage.jsx";
import PetDetails from "./pets/pages/PetDetails.jsx";
import PetForm from "./pets/pages/PetForm.jsx";
import PetList from "./pets/pages/PetList.jsx";
import PetUpdateForm from "./pets/pages/PetUpdateForm.jsx";
import ProfilePage from "./Auth/pages/ProfilePage.jsx";
import MascotaPage from "./pets/pages/MascotaPage.jsx";


function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/refugio" element={<RefugioPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/adoptante" element={<AdoptantePage />} />
          <Route path="/editar/:id" element={<PetUpdateForm/>} />
          <Route path="/ver" element={<PetList/>} />
          <Route path= "/crear" element={<PetForm/>}/>
          <Route path= "/profile" element={<ProfilePage/>}/>
          <Route path= "/mascotas" element={<MascotaPage/>}/>
          <Route path="/" element={<HomePage/>} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;