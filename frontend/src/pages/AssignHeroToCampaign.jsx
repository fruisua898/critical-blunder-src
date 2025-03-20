import React, { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import "../styles/styles.scss";
import extractErrorMessage from "../utils/extractErrorMessage";
import { searchHeroes } from "../services/heroService";
import { assignHeroToCampaign } from "../services/campaignService";

const AssignHeroToCampaign = () => {
  const { campaignId } = useParams();
  const navigate = useNavigate();

  const [filterName, setFilterName] = useState("");
  const [filterClass, setFilterClass] = useState("");
  const [heroes, setHeroes] = useState([]);
  const [errorMsg, setErrorMsg] = useState("");

  const handleSearch = async () => {
    setErrorMsg("");
    try {
      const results = await searchHeroes(filterName, filterClass);
      setHeroes(results);
    } catch (error) {
      setErrorMsg(error.message);
    }
  };

  const handleAssign = async (heroId) => {
    try {
      await assignHeroToCampaign(campaignId, heroId);
      setHeroes((prev) => prev.filter((hero) => hero.id !== heroId));
    } catch (error) {
      const errMsg = extractErrorMessage(error);
      setErrorMsg(errMsg);
    }
  };

  return (
    <div className="assign-hero">
      <h2>Asignar Héroe a la Campaña</h2>
      {errorMsg && <div className="error">{errorMsg}</div>}
      <div className="filters">
        <input
          type="text"
          placeholder="Nombre del usuario"
          value={filterName}
          onChange={(e) => setFilterName(e.target.value)}
        />
        <select
          value={filterClass}
          onChange={(e) => setFilterClass(e.target.value)}
        >
          <option value="">Todas las Clases</option>
          <option value="CLERIC">Clérigo</option>
          <option value="BARD">Bardo</option>
          <option value="DRUID">Druida</option>
          <option value="WARLOCK">Brujo</option>
          <option value="RANGER">Explorador</option>
          <option value="BARBARIAN">Bárbaro</option>
          <option value="SORCERER">Hechicero</option>
          <option value="ROGUE">Pícaro</option>
          <option value="WIZARD">Mago</option>
          <option value="FIGHTER">Guerrero</option>
          <option value="MONK">Monje</option>
          <option value="PALADIN">Paladin</option>
        </select>
        <button onClick={handleSearch}>Buscar</button>
      </div>
      <div className="hero-list">
        {heroes.length > 0 ? (
          heroes.map((hero) => (
            <div className="hero-item" key={hero.id}>
              <span className="hero-name">
                {hero.name} - {hero.heroClass}
              </span>
              <button onClick={() => handleAssign(hero.id)}>Asignar</button>
            </div>
          ))
        ) : (
          <p>No se encontraron héroes.</p>
        )}
      </div>
      <button className="back-button" onClick={() => navigate(-1)}>
        Volver
      </button>
    </div>
  );
};

export default AssignHeroToCampaign;