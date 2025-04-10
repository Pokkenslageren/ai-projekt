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

        // Disable button while processing
        sendBtn.disabled = true;
        responseOutput.innerHTML = "🐷 Tænker...";

        try {
            const res = await fetch(`/api/movies/explore?title=${encodeURIComponent(movieName)}`);
            const data = await res.json();

            if (!res.ok) {
                throw new Error(data.error || "Kunne ikke finde filmen");
            }

            // format
            const formattedResponse = `
                🎬 ${data.title} (${data.releaseDate})
                ${data.tagline ? `"${data.tagline}"` : ''}
                
                ⭐ Bedømmelse: ${data.rating}
                ⏱️ Længde: ${data.runtime}
                
                🐷 Kinogrisen siger:
                ${data.interestingTrivia}
                
                👥 Lignende film:
                ${data.similarMovies.map(movie =>
                `- ${movie.title} (${movie.releaseDate})`
            ).join('\n')}
            `;

            responseOutput.innerHTML = formattedResponse;
        } catch (err) {
            responseOutput.innerHTML = `🐷 ${err.message}`;
        } finally {
            // Re-enable button after processing
            sendBtn.disabled = false;
        }
    });
});