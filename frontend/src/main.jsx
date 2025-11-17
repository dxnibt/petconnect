import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import App from "./App";
import AdoptanteForm from "./components/AdoptanteForm";
import RefugioForm from "./components/RefugioForm";

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<App />} />
        <Route path="/adoptante" element={<AdoptanteForm />} />
        <Route path="/refugio" element={<RefugioForm />} />
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);
