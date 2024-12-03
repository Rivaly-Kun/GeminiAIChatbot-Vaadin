import express from "express";
import { GoogleGenerativeAI } from "@google/generative-ai";

const app = express();
const apiKey = process.env.GEMINI_API_KEY || "AIzaSyCstAurqINn3WIhhC1nV5uqczCV8wbw-0s"; // Use an environment variable or replace with your actual API key
const genAI = new GoogleGenerativeAI(apiKey);

const generationConfig = {
  temperature: 1,
  topP: 0.95,
  topK: 64,
  maxOutputTokens: 8192,
  responseMimeType: "text/plain",
};

app.use(express.json());

app.post("/generate", async (req, res) => {
  const { prompt } = req.body;

  if (!prompt) {
    return res.status(400).json({ error: "Prompt is required." });
  }

  try {
    const model = await genAI.getGenerativeModel({ model: "gemini-exp-1114" });
    
    const chatSession = model.startChat({
      generationConfig,
      history: [],
    });

    const result = await chatSession.sendMessage(prompt);

    res.json({ response: result.response.text() });
  } catch (error) {
    console.error("Error generating content:", error.message);
    res.status(500).json({ error: "An error occurred while generating content." });
  }
});

app.listen(3000, () => {
  console.log("Gemini AI server running on http://localhost:3000");
});
