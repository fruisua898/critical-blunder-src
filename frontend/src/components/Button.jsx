import React from "react";
import PropTypes from "prop-types";
import "../styles/styles.scss";

const Button = ({ children, onClick, type = "primary", disabled = false }) => (
  <button className={`btn btn-${type}`} onClick={onClick} disabled={disabled}>
    {children}
  </button>
);

Button.propTypes = {
  children: PropTypes.node.isRequired,
  onClick: PropTypes.func,
  type: PropTypes.oneOf(["primary", "secondary"]),
  disabled: PropTypes.bool,
};

export default Button;