import React from 'react';
import './NavBar.css'

function NavBar() {
return (
<nav className="nav-bar">
<ul className="nav-list">
<li className="nav-item"><a href="/">Home</a></li>
<li className="nav-item"><a href="#">Explore</a></li>
<li className="nav-item"><a href="#">Developer</a></li>
</ul>
</nav>
)
}

export default NavBar;