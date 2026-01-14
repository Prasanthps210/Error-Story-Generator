import { useState } from "react";
import { register } from "../api/api";

export default function Register({ setPage }) {
    const [form, setForm] = useState({
        username: "",
        email: "",
        password: ""
    });

    const handleRegister = async () => {
        try {
            await register(form);
            alert("Registration successful!");
            setPage("login");
        } catch {
            alert("Registration failed");
        }
    };

    return (
        <div style={{ maxWidth: 400, margin: "100px auto" }}>
            <h2>Register</h2>

            <input
                placeholder="Username"
                value={form.username}
                onChange={(e) => setForm({ ...form, username: e.target.value })}
            />

            <input
                placeholder="Email"
                value={form.email}
                onChange={(e) => setForm({ ...form, email: e.target.value })}
            />

            <input
                type="password"
                placeholder="Password"
                value={form.password}
                onChange={(e) => setForm({ ...form, password: e.target.value })}
            />

            <button onClick={handleRegister}>Create Account</button>

            <p>
                Already have account?{" "}
                <button onClick={() => setPage("login")}>Login</button>
            </p>
        </div>
    );
}
