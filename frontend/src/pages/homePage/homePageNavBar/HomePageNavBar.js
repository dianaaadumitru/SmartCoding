import React from 'react';

import './HomePageNavBar.css'

function HomePageNavBar() {
    return (
        <div>
            <nav>
                <ul>
                    <li><a href="#">Explore</a></li>
                    <li><a href="#">Developer</a></li>
                    <li><a href="/signin">Sign in</a></li>
                </ul>
            </nav>
        </div>
    )
}

export default HomePageNavBar;