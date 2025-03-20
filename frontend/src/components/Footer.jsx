import React from "react";
import "../styles/styles.scss";

import patreonIcon from "../assets/patreon.svg";
import instagramIcon from "../assets/instagram.svg";
import facebookIcon from "../assets/facebook.svg";
import twitterIcon from "../assets/twitter.svg";
import gmailIcon from "../assets/gmail.svg";

const Footer = () => {
  return (
    <footer>
      <div className="social-icons">
        <a
          href="https://www.patreon.com"
          target="_blank"
          rel="noopener noreferrer"
        >
          <img src={patreonIcon} alt="Patreon" />
        </a>
        <a
          href="https://www.instagram.com"
          target="_blank"
          rel="noopener noreferrer"
        >
          <img src={instagramIcon} alt="Instagram" />
        </a>
        <a
          href="https://www.facebook.com"
          target="_blank"
          rel="noopener noreferrer"
        >
          <img src={facebookIcon} alt="Facebook" />
        </a>
        <a href="https://twitter.com" target="_blank" rel="noopener noreferrer">
          <img src={twitterIcon} alt="Twitter" />
        </a>
        <a
          href="https://mail.google.com"
          target="_blank"
          rel="noopener noreferrer"
        >
          <img src={gmailIcon} alt="Gmail" />
        </a>
      </div>

      <div className="legal-links">
        <a href="/aviso-legal">Aviso legal</a>
        <a href="/privacidad">Privacidad</a>
        <a href="/cookies">Cookies</a>
      </div>
    </footer>
  );
};

export default Footer;