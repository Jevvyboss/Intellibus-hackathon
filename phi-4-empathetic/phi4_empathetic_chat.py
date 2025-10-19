import os
from flask import Flask, request, jsonify
from flask_cors import CORS
from llama_cpp import Llama
from huggingface_hub import snapshot_download
import threading

# ---------------- Configuration ----------------
REPO_ID = "Triangle104/Phi-4-Empathetic-Q4_K_S-GGUF"
LOCAL_DIR = "models/phi4_empathetic"
HF_TOKEN = os.getenv("HUGGINGFACE_TOKEN")

if not HF_TOKEN:
    raise ValueError("No Hugging Face token found. Please export HUGGINGFACE_TOKEN or use huggingface-cli login.")

# 1) Download model if not already present
print(f"Checking/downloading model: {REPO_ID} ...")
model_dir = snapshot_download(repo_id=REPO_ID, local_dir=LOCAL_DIR, token=HF_TOKEN)
print(f"Model files at: {model_dir}")

# 2) Find GGUF file
gguf_files = [f for f in os.listdir(model_dir) if f.endswith(".gguf")]
if not gguf_files:
    raise FileNotFoundError("No .gguf file found in the model directory! Check the model repo.")
model_path = os.path.join(model_dir, gguf_files[0])
print(f"Using model file: {model_path}")

# 3) Load model
print("Loading model (this may take a while)...")
llm = Llama(model_path=model_path, n_ctx=4096, verbose=False)
print("Model loaded.")

# System prompt for consistent empathetic persona
system_prompt = (
    "You are Phi-4 Empathetic, a warm, gentle, and understanding emotional support companion. "
    "You listen carefully, validate feelings, and respond with kindness and reassurance. "
    "Avoid giving professional mental health advice; encourage seeking professional help when appropriate."
)

# ---------------- Flask API ----------------
app = Flask(__name__)
CORS(app)
lock = threading.Lock()
conversations = {}
distress_keywords = ["kill myself", "suicide", "end my life", "want to die", "can't go on"]

def detect_distress(message):
    return any(k in message.lower() for k in distress_keywords)

@app.route("/chat", methods=["POST"])
def chat():
    data = request.get_json()
    if not data or "message" not in data or "user_id" not in data:
        return jsonify({"error": "Missing 'message' or 'user_id'"}), 400

    user_message = data["message"].strip()
    user_id = str(data["user_id"]).strip()

    if user_id not in conversations:
        conversations[user_id] = []

    # Crisis handling
    if detect_distress(user_message):
        response_text = (
            "I'm really sorry that you're feeling like this. 😔 It's important to talk to someone "
            "who can provide the right support. If you’re in immediate danger, please contact "
            "emergency services or go to the nearest hospital.\n\n"
            "Before we continue, I want to remind you that I'm not a substitute for professional "
            "mental health advice. Please reach out to a trusted friend, family member, or mental health professional.\n\n"
            "📞 Jamaica: 888-639-5433\n📞 US: 988\n🌐 Worldwide: https://findahelpline.com\n\n"
            "Would you like to tell me what’s been happening lately?"
        )
    else:
        conversations[user_id].append({"role": "user", "content": user_message})
        # Include last 6 messages for context
        chat_history = "\n".join(
            [f"{msg['role'].capitalize()}: {msg['content']}" for msg in conversations[user_id][-6:]]
        )

        prompt = f"{system_prompt}\n\nConversation so far:\n{chat_history}\nAssistant:"
        with lock:
            output = llm.create_completion(
                prompt=prompt,
                max_tokens=300,
                temperature=0.8,
                top_p=0.95
            )
        response_text = output["choices"][0]["text"].strip()
        conversations[user_id].append({"role": "assistant", "content": response_text})

    return jsonify({"response": response_text, "conversation_length": len(conversations[user_id])})

@app.route("/reset", methods=["POST"])
def reset_conversation():
    data = request.get_json()
    if not data or "user_id" not in data:
        return jsonify({"error": "Missing 'user_id'"}), 400

    user_id = str(data["user_id"]).strip()
    if user_id in conversations:
        del conversations[user_id]
        return jsonify({"status": "Conversation reset."})
    else:
        return jsonify({"status": "No existing conversation for this user."})

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
