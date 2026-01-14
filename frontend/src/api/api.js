const BASE_URL = "http://localhost:8080/api";

// Register user
export async function register(user) {
    const res = await fetch(`${BASE_URL}/auth/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(user),
    });

    if (!res.ok) throw new Error("Registration failed");
    return res.json();
}

// Login user
export async function login(data) {
    const res = await fetch(`${BASE_URL}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
    });

    if (!res.ok) throw new Error("Login failed");
    return res.json();
}

// Generate story
export async function generateStory(userId, payload) {
    const res = await fetch(`${BASE_URL}/story/generate/${userId}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
    });

    if (!res.ok) throw new Error("Story generation failed");
    return res.json();
}

// Get history (library)
export async function getHistory(userId) {
    const res = await fetch(`${BASE_URL}/story/history/${userId}`);

    if (!res.ok) throw new Error("Failed to fetch history");
    return res.json();
}
