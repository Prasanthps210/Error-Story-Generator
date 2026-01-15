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

    const text = await res.text();

    if (!res.ok) {
        throw new Error(text);
    }

   // return res.json();
    return JSON.parse(text);
}

export async function login(credentials) {
    const res = await fetch(`${BASE_URL}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(credentials),
    });

    const text = await res.text();

    if (!res.ok) {
        throw new Error(text);
    }

 //   return res.json();
    return JSON.parse(text);
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
// DELETE STORY
export async function deleteStory(id) {
    const res = await fetch(`${BASE_URL}/story/delete/${id}`, {
        method: "DELETE",
        headers: getAuthHeaders(),
    });

    if (!res.ok) {
        throw new Error("Delete failed");
    }

    return res.text();
}

// UPDATE STORY
export async function updateStory(id, updatedStory) {
    const res = await fetch(`${BASE_URL}/story/update/${id}`, {
        method: "PUT",
        headers: getAuthHeaders(),
        body: JSON.stringify(updatedStory),
    });

    if (!res.ok) {
        throw new Error("Update failed");
    }

    return res.json();
}
