import { useState, useEffect } from "react";
import axios from "axios";
import "../styles/adoptante/adoptante.css"; 

function RefugioForm(){ 
  const [datosBasicos, setDatosBasicos] = useState({});
  const [form, setForm] = useState({
    nit:"",
    website:"",
    supportDocument:"",
    shelterDescription:""
  });

useEffect(() => {
    const data = localStorage.getItem("datosBasicos");
    if (data) setDatosBasicos(JSON.parse(data));
  }, []);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm({
      ...form,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const adoptante = { ...datosBasicos, ...form };

    try {
      const response = await axios.post(
        "http://localhost:8181/api/petconnect/refugios/save",
        refugio
      );
      alert("Refugio registrado con éxito!");
      console.log(response.data);
      localStorage.removeItem("datosBasicos");
      setForm({
        nit:"",
        website:"",
        supportDocument:"",
        shelterDescription:""
        
      });
    } catch (error) {
      console.error(error);
      alert("Error al registrar refugio");
    }
  };
}
export default RefugioForm;
