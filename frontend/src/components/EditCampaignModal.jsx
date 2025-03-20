import React, { useState } from "react";
import "../styles/_modal.scss";

const EditCampaignModal = ({ campaign, onClose, onSave }) => {
  const [name, setName] = useState(campaign.name);
  const [description, setDescription] = useState(campaign.description);
  const [status, setStatus] = useState(campaign.status);

  const handleSubmit = (e) => {
    e.preventDefault();
    onSave({ name, description, status });
  };

  return (
    <div className="modal-overlay">
      <div className="edit-campaign-modal">
        <h3>Editar Campaña: {campaign.name}</h3>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Nombre:</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label>Descripción:</label>
            <textarea
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              required
            ></textarea>
          </div>
          <div className="form-group">
            <label>Estado:</label>
            <select
              value={status}
              onChange={(e) => setStatus(e.target.value)}
              required
            >
              <option value="ACTIVE">Active</option>
              <option value="FINISHED">Finished</option>
              <option value="PAUSED">Paused</option>
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

export default EditCampaignModal;