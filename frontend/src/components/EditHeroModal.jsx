import React, { useState } from "react";
import "../styles/_modal.scss";

const EditHeroModal = ({ hero, onClose, onSave }) => {
  const [level, setLevel] = useState(hero.level);
  const [experience, setExperience] = useState(hero.experience);
  const [status, setStatus] = useState(hero.status);

  const handleSubmit = (e) => {
    e.preventDefault();
    onSave({ level, experience, status });
  };

  return (
    <div className="modal-overlay">
      <div className="edit-hero-modal">
        <h3>Editar Héroe: {hero.HeroName}</h3>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Nivel:</label>
            <input
              type="number"
              value={level}
              onChange={(e) => setLevel(Number(e.target.value))}
              required
            />
          </div>
          <div className="form-group">
            <label>Experiencia:</label>
            <input
              type="number"
              value={experience}
              onChange={(e) => setExperience(Number(e.target.value))}
              required
            />
          </div>
          <div className="form-group">
            <label>Estado:</label>
            <select
              value={status}
              onChange={(e) => setStatus(e.target.value)}
              required
            >
              <option value="ALIVE">ALIVE</option>
              <option value="DEAD">DEAD</option>
              <option value="RETIRED">RETIRED</option>
            </select>
          </div>
          <div className="modal-actions">
            <button type="submit">Guardar</button>
            <button type="button" onClick={onClose}>
              Cancelar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EditHeroModal;