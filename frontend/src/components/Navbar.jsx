export default function Navbar({ setPage, handleLogout }) {
    return (
        <div
            style={{
                padding: "12px 20px",
                borderBottom: "1px solid #334155",
                display: "flex",
                justifyContent: "flex-end",
                gap: "12px"
            }}
        >
            <button onClick={() => setPage("dashboard")}>Home</button>
            <button onClick={() => setPage("library")}>Library</button>
            <button onClick={handleLogout}>Logout</button>
        </div>
    );
}
