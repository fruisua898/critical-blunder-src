import React, { useState, useEffect } from "react";
import { useParams, useNavigate, useSearchParams } from "react-router-dom";
import { getHero, createHero, updateHero } from "../services/characterService";
import "../styles/styles.scss";

const CharacterPage = () => {
  const navigate = useNavigate();
  const { heroId } = useParams();
  const [searchParams] = useSearchParams();
  const [mode, setMode] = useState("create");

  const [name, setName] = useState("");
  const [heroClass, setHeroClass] = useState("FIGHTER");
  const [description, setDescription] = useState("");
  const [age, setAge] = useState("");
  const [appearance, setAppearance] = useState("");

  const [errorMsg, setErrorMsg] = useState("");
  const [successMsg, setSuccessMsg] = useState("");

  useEffect(() => {
    if (heroId) {
      const queryMode = searchParams.get("mode");
      setMode(queryMode === "view" ? "view" : "update");
      getHero(heroId)
        .then((hero) => {
          setName(hero.name || "");
          setHeroClass(hero.HeroClass || hero.heroClass || "FIGHTER");
          setDescription(hero.description || "");
          setAge(hero.age || "");
          setAppearance(hero.appearance || "");
        })
        .catch((err) => {
          setErrorMsg(err.message);
        });
    } else {
      setMode("create");
    }
  }, [heroId, searchParams]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMsg("");
    setSuccessMsg("");

    if (name.trim().length < 3) {
      setErrorMsg("El nombre debe tener al menos 3 caracteres.");
      return;
    }

    try {
      let message = "";
      if (mode === "create") {
        message = await createHero({
          name,
          heroClass,
          description,
          age: age ? Number(age) : null,
          appearance,
        });
        setSuccessMsg(message);
        setTimeout(() => {
          navigate(-1);
        }, 1500);
      } else if (mode === "update") {
        message = await updateHero(heroId, {
          name,
          description,
          age: age ? Number(age) : null,
          appearance,
        });
        setSuccessMsg(message);
      }
    } catch (err) {
      setErrorMsg(err.message);
    }
  };

  const fieldsDisabled = mode === "view";

  return (
    <section className="character-page">
      <div className="character-content">
        <h2>
          {mode === "create"
            ? "Crear Personaje"
            : mode === "update"
            ? "Actualizar Personaje"
            : "Ver Detalle del Personaje"}
        </h2>

        <div className="form-section">
          <label>Nombre</label>
          <input
            type="text"
            placeholder="Nombre del personaje"
            value={name}
            onChange={(e) => setName(e.target.value)}
            disabled={fieldsDisabled}
          />

          <label>Clase</label>
          {mode === "create" ? (
            <select
              value={heroClass}
              onChange={(e) => setHeroClass(e.target.value)}
              disabled={fieldsDisabled}
            >
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
          ) : (
            <input type="text" value={heroClass} disabled />
          )}

          <label>Edad</label>
          <input
            type="number"
            placeholder="Edad"
            value={age}
            onChange={(e) => setAge(e.target.value)}
            disabled={fieldsDisabled}
            min="1"
            max="500"
          />

          <label>Descripción</label>
          <textarea
            placeholder="Escribe la descripción..."
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            disabled={fieldsDisabled}
          ></textarea>

          <label>Apariencia</label>
          <textarea
            placeholder="Describe la apariencia..."
            value={appearance}
            onChange={(e) => setAppearance(e.target.value)}
            disabled={fieldsDisabled}
          ></textarea>

          <div className="button-row">
            <button className="back-button" onClick={() => navigate(-1)}>
              Volver
            </button>
            {(mode === "create" || mode === "update") && (
              <button
                className="submit-btn"
                type="submit"
                onClick={handleSubmit}
              >
                {mode === "create" ? "Enviar" : "Actualizar"}
              </button>
            )}
          </div>
        </div>

        {errorMsg && <div className="error">{errorMsg}</div>}
        {successMsg && <div className="success">{successMsg}</div>}
      </div>
    </section>
  );
};

export default CharacterPage;