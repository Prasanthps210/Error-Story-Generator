const BASE_URL = "http://localhost:8080/api";

// Safe header builder
function getAuthHeaders() {
    const token = localStorage.getItem("token");
    return {
        "Content-Type": "application/json",
        ...(token && { Authorization: `Bearer ${token}` })
    };
}

// ---------- AUTH ----------

export async function register(user) {
    const res = await fetch(`${BASE_URL}/auth/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(user),
    });

    if (!res.ok) {
        throw new Error("Registration failed");
    }

    return res.json();
}

export async function login(credentials) {
    const res = await fetch(`${BASE_URL}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(credentials),
    });

    if (!res.ok) {
        throw new Error("Login failed");
    }

    return res.json();
}

// ---------- STORY ----------

export async function generateStory(userId, payload) {
    const res = await fetch(`${BASE_URL}/story/generate/${userId}`, {
        method: "POST",
        headers: getAuthHeaders(),
        body: JSON.stringify(payload),
    });

    if (!res.ok) {
        throw new Error("Generation failed");
    }

    return res.json();
}

export async function getHistory(userId) {
    const res = await fetch(`${BASE_URL}/story/history/${userId}`, {
        headers: getAuthHeaders(),
    });

    if (!res.ok) {
        throw new Error("History fetch failed");
    }

    return res.json();
}
