document.addEventListener("DOMContentLoaded", () => {
    const sendBtn = document.getElementById("sendBtn");
    const userInput = document.getElementById("userInput");
    const responseOutput = document.getElementById("responseOutput");

    sendBtn.addEventListener("click", async () => {
        const movieName = userInput.value.trim();
        if (!movieName) {
            responseOutput.innerHTML = "🐷 Skriv venligst en film!";
            return;
        }

        responseOutput.innerHTML = "🐷 Tænker...";

        try {
            const res = await fetch(`/api/movies/explore?title=${encodeURIComponent(movieName)}`);

            // const res = await fetch(`/kinogrisen?movie=${encodeURIComponent(movieName)}`);

            const data = await res.json();

            const reply = data.Choices?.[0]?.message?.content || "🐷 Intet svar!";
            responseOutput.innerHTML = `🐷 ${reply}`;
        } catch (err) {
            responseOutput.innerHTML = `🐷 Fejl: ${err.message}`;
        }
    });
});