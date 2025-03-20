import React, { useState } from "react";
import { login, register } from "../services/authService";
import Input from "../components/Input";
import Button from "../components/Button";
import { Link, useLocation, useNavigate } from "react-router-dom";
import "../styles/styles.scss";
import extractErrorMessage from "../utils/extractErrorMessage";

const AuthPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const isLogin = location.pathname === "/login";

  const [email, setEmail] = useState("");
  const [name, setName] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errors, setErrors] = useState([]);
  const [success, setSuccess] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors([]);
    setSuccess("");

    try {
      if (isLogin) {
        await login(email, password);
        navigate("/dashboard");
      } else {
        if (password !== confirmPassword) {
          setErrors(["Las contraseñas no coinciden."]);
          return;
        }
        const message = await register(email, name, password);
        setSuccess(message);
        setTimeout(() => navigate("/login"), 1500);
      }
    } catch (err) {
      const errorMessage = extractErrorMessage(err);
      setErrors([errorMessage]);
    }
  };

  return (
    <section className="auth">
      <h1>{isLogin ? "Iniciar Sesión" : "Registrarse"}</h1>
      <form onSubmit={handleSubmit}>
        <Input
          label="Correo"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        {!isLogin && (
          <Input
            label="Nombre"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        )}
        <Input
          label="Contraseña"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        {!isLogin && (
          <Input
            label="Confirmar Contraseña"
            type="password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
          />
        )}

        {errors.length > 0 && (
          <div className="error">
            <ul>
              {errors.map((msg, index) => (
                <li key={index}>
                  {msg.charAt(0).toUpperCase() + msg.slice(1)}
                </li>
              ))}
            </ul>
          </div>
        )}

        {success && <p className="success">{success}</p>}

        <Button type="submit">{isLogin ? "Entrar" : "Registrarse"}</Button>
      </form>

      {isLogin ? (
        <p className="auth-switch">
          ¿Aún no tienes cuenta? <Link to="/register"> Regístrate aquí </Link>
        </p>
      ) : (
        <p className="auth-switch">
          ¿Ya tienes cuenta? <Link to="/login"> Inicia sesión aquí </Link>
        </p>
      )}

      {isLogin && (
        <p>
          <a href="#">¿Olvidaste tu contraseña?</a>
        </p>
      )}
    </section>
  );
};

export default AuthPage;