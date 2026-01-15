import { useState } from "react";
import { login } from "../api/api";

export default function Login({ setUser, setPage }) {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const handleLogin = async () => {
        try {
            const data = await login({ username, password });

            // Save JWT + user in localStorage
            localStorage.setItem("token", data.token);
            localStorage.setItem("user", JSON.stringify(data.user));

            setUser(data.user);
            setPage("dashboard");
        } catch (e) {
            alert(e.message);
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-card">
                <h2>Login</h2>

                <input
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />

                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />

                <button onClick={handleLogin}>Login</button>

                <p>
                    No account?{" "}
                    <button className="link-btn" onClick={() => setPage("register")}>
                        Register
                    </button>
                </p>
            </div>
        </div>
    );

}
