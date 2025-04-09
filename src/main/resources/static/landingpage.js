document.addEventListener("DOMContentLoaded", () => {
    const sendBtn = document.getElementById("sendBtn");
    const userInput = document.getElementById("userInput");
    const responseOutput = document.getElementById("responseOutput");

    sendBtn.addEventListener("click", async () => {
<<<<<<< HEAD
        const message = userInput.value.trim();
        if (!message) {
            responseOutput.innerHTML = "🐷 Please enter something!";
            return;
        }

        responseOutput.innerHTML = "🐷 Thinking...";

        try {
            const res = await fetch(`/chat1?message=${encodeURIComponent(message)}`);
            const data = await res.json();
            const reply = data[0]?.message?.content || "No response from pig 🐷";
=======
        const movieName = userInput.value.trim();
        if (!movieName) {
            responseOutput.innerHTML = "🐷 Please enter a movie!";
            return;
        }

        responseOutput.innerHTML = "🐷 Tænker...";

        try {
            const res = await fetch(`/test?movie=${encodeURIComponent(movieName)}`);

            const data = await res.json();

            const reply = data.Choices?.[0]?.message?.content || "🐷 Intet svar!";
>>>>>>> b8ddc8cc02d5e431f38a8ad03d865c10ea9a8745
            responseOutput.innerHTML = `🐷 ${reply}`;
        } catch (err) {
            responseOutput.innerHTML = `🐷 Error: ${err.message}`;
        }
    });
});
