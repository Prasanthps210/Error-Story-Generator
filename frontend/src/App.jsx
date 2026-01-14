import { useEffect, useState } from "react";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import Library from "./pages/Library";
import Navbar from "./components/Navbar";

export default function App() {
    const [user, setUser] = useState(null);
    const [page, setPage] = useState("login");
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const savedUser = localStorage.getItem("user");
        const token = localStorage.getItem("token");

        if (savedUser && token) {
            setUser(JSON.parse(savedUser));
            setPage("dashboard");
        }

        setLoading(false);
    }, []);

    if (loading) {
        return <h2 style={{ color: "white" }}>Loading...</h2>;
    }

    if (!user) {
        return page === "login"
            ? <Login setUser={setUser} setPage={setPage} />
            : <Register setPage={setPage} />;
    }

    return (
        <div>
            <Navbar setPage={setPage} handleLogout={() => {
                localStorage.clear();
                setUser(null);
                setPage("login");
            }} />

            {page === "dashboard" && <Dashboard user={user} />}
            {page === "library" && <Library user={user} />}
        </div>
    );
}
