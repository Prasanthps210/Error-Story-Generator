export default function Navbar({ setPage, handleLogout }) {
    return (
        <div style={{ padding: 10, borderBottom: "1px solid gray" }}>
            <button onClick={() => setPage("dashboard")}>Generate</button>
            <button onClick={() => setPage("library")}>Library</button>
            <button onClick={handleLogout}>Logout</button>
        </div>
    );
}
