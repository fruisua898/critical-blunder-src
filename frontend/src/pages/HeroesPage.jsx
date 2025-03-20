import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import NavBar from "../components/NavBar";
import MobileBackButton from "../components/MobileBackButton";
import extractErrorMessage from "../utils/extractErrorMessage";
import { getUserHeroes, deleteHero } from "../services/characterService";
import "../styles/styles.scss";

import { FaTrash, FaEdit, FaArrowRight } from "react-icons/fa";

const HeroesPage = () => {
  const [heroes, setHeroes] = useState([]);
  const [errorMsg, setErrorMsg] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const heroesPerPage = 5;
  const navigate = useNavigate();

  useEffect(() => {
    getUserHeroes()
      .then((data) => {
        setHeroes(data || []);
      })
      .catch((err) => {
        setErrorMsg(err.message);
      });
  }, []);

  const handleDelete = async (heroId) => {
    try {
      await deleteHero(heroId);
      setHeroes((prev) => prev.filter((h) => h.id !== heroId));
    } catch (err) {
      const errorMessage = extractErrorMessage(err);
      setErrorMsg(errorMessage);
    }
  };

  const handleEdit = (heroId) => {
    navigate(`/character/${heroId}`);
  };

  const handleDetail = (heroId) => {
    navigate(`/character/${heroId}?mode=view`);
  };

  const handleCreate = () => {
    navigate("/character");
  };

  const totalPages = Math.ceil(heroes.length / heroesPerPage);
  const paginatedHeroes = heroes.slice(
    currentPage * heroesPerPage,
    (currentPage + 1) * heroesPerPage
  );

  return (
    <section className="heroes-page">
      <div className="heroes-content">
        <h1>Mis Héroes</h1>

        {errorMsg && <div className="error">{errorMsg}</div>}

        <div className="hero-list">
          {paginatedHeroes.map((hero) => (
            <div className="hero-item" key={hero.id}>
              <div className="hero-name">{hero.name}</div>
              <div className="hero-buttons">
                <button
                  className="btn-delete"
                  onClick={() => handleDelete(hero.id)}
                >
                  <FaTrash />
                </button>
                <button
                  className="btn-edit"
                  onClick={() => handleEdit(hero.id)}
                >
                  <FaEdit />
                </button>
                <button
                  className="btn-detail"
                  onClick={() => handleDetail(hero.id)}
                >
                  <FaArrowRight />
                </button>
              </div>
            </div>
          ))}
        </div>

        <div className="pagination">
          <button
            disabled={currentPage === 0}
            onClick={() => setCurrentPage(currentPage - 1)}
          >
            ← Anterior
          </button>
          <span>
            Página {currentPage + 1} de {totalPages}
          </span>
          <button
            disabled={currentPage >= totalPages - 1}
            onClick={() => setCurrentPage(currentPage + 1)}
          >
            Siguiente →
          </button>
        </div>

        <button className="add-entity" onClick={handleCreate}>
          Agregar Nuevo Héroe
        </button>
        <MobileBackButton />
      </div>

      <div className="nav-right">
        <NavBar />
      </div>
    </section>
  );
};

export default HeroesPage;