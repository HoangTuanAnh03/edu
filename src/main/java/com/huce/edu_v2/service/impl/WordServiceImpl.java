package com.huce.edu_v2.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huce.edu_v2.dto.response.word.QuestionResponse;
import com.huce.edu_v2.entity.History;
import com.huce.edu_v2.entity.Word;
import com.huce.edu_v2.repository.HistoryRepository;
import com.huce.edu_v2.repository.TopicRepository;
import com.huce.edu_v2.repository.WordRepository;
import com.huce.edu_v2.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class WordServiceImpl implements WordService {
	@Autowired
	WordRepository wordRepository;

	@Autowired
	TopicRepository topicRepository;

	@Autowired
	HistoryRepository historyRepository;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	final RestTemplate restTemplate;

	public WordServiceImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

//	String API_URL_TEMPLATE = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=%s";

//	@Value("${GEMINI_API_KEY}")
//	private String geminiKey;
//
//	@Value("${OPENAI_KEY}")
//	private String openaiKey;

	@Value("${GROQ_KEY}")
	private String groqKey;

//	@Value("${OPENROUTER}")
//	private String openRouter;

//	public WordServiceImpl() {
//		this.restTemplate = new RestTemplate();
//	}

	@Override
	public Word findFirstWordBeforeWid(Integer wid, Integer tid){
		return wordRepository.findFirstByWidGreaterThanAndTopic(wid, topicRepository.findFirstByTid(tid)).orElse(null);
	}

	public QuestionResponse getWordDefault(Word word){
		QuestionResponse questionResponse = new QuestionResponse();
		String question = switch ((int) (Math.random() * 3)) {
			case 1 -> "Từ nào có nghĩa: " + word.getViedesc();
			case 2 -> "Hãy chọn từ được nói trong: " + word.getVoice();
			default -> "Đây là phiên âm của từ nào: /" + word.getPronun() + "/ ";
		};
		List<String> answers = new ArrayList<>(wordRepository.get3RandomWordsByTid(word.getTopic().getTid() + 1).stream().map(Word::getWord).toList());
		if(answers.isEmpty()){
			answers = new ArrayList<>(wordRepository.get3RandomWordsByTid(word.getTopic().getTid() - 1).stream().map(Word::getWord).toList());
		}
		answers.add((int) (Math.random()*4), word.getWord());
		questionResponse.setWid(word.getWid());
		questionResponse.setQuestion(question);
		questionResponse.setAnswers(answers);
		return questionResponse;
	}

	public String getPrompt(String word){
		String prompt = """
				"Generate an English multiple-choice fill-in-the-blank question at the A2 level with the given correct answer. The question should assess grammar, parts of speech, or the contextual meaning of the word. Follow these guidelines:
				Use the provided correct answer to fill the blank meaningfully in a sentence.
				Create a sentence where the blank can only be completed correctly by the provided answer, requiring a grammatical or semantic understanding.
				Provide four answer choices (a, b, c, d), with only one correct option and three plausible but incorrect ones.
				Here is the correct answer: %s. Please answer in the structure only.
				Structure the response like this:
				Question: [Sentence with blank]
				Choices: a) [option 1], b) [option 2], c) [option 3], d) [option 4]
				""";
		prompt = String.format(prompt, word);
		return prompt;
	}


/*
	@Override
	public QuestionResponse getQuestion(Integer wid, Integer tid) {
		Word word = findFirstWordBeforeWid(wid, tid);
		String prompt = getPrompt(word.getWord());
		String apiUrl = String.format(API_URL_TEMPLATE, geminiKey);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		ObjectNode contentNode = objectMapper.createObjectNode();
		ObjectNode partsNode = objectMapper.createObjectNode();
		partsNode.put("text", prompt);
		contentNode.set("parts", objectMapper.createArrayNode().add(partsNode));
		ObjectNode requestBodyNode = objectMapper.createObjectNode();
		requestBodyNode.set("contents", objectMapper.createArrayNode().add(contentNode));
		String requestBody;
		try {
			requestBody = objectMapper.writeValueAsString(requestBodyNode);
		} catch (Exception e) {
			return getWordDefault(word);
		}
		HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
		ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);
		try {
			JsonNode jsonResponse = objectMapper.readTree(response.getBody());
			String text = jsonResponse.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText().replace("\n", " ").replace("  ", " ");
			String[] res = text.split(" Choices: a\\) ");
			String question = res[0].replace("Question: ", "");
			String[] choices = res[1].replace(",","").split(" [b-d]\\) ");
			return new QuestionResponse(word.getWid(), question, Arrays.stream(choices).toList());
		} catch (Exception e) {
			return getWordDefault(word);
		}
	}
*/

	@Override
	public QuestionResponse getQuestion(Integer wid, Integer tid){
		Word word = findFirstWordBeforeWid(wid, tid);
		String prompt = getPrompt(word.getWord());
		var body = """
                {
                    "model": "gemma2-9b-it",
                    "messages": [
                        {
                            "role": "user",
                            "content": "%s"
                        }
                    ]
                }""";
		body = String.format(body, escapeJson(prompt));
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
				.header("Content-Type", "application/json")
				.header("Authorization", "Bearer " + groqKey)
				.POST(HttpRequest.BodyPublishers.ofString(body))
				.build();
		var client = HttpClient.newHttpClient();
		HttpResponse<String> response;
		try{
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		}catch (Exception e){
			return getWordDefault(word);
		}
		try {
			JsonNode jsonResponse = objectMapper.readTree(response.body());
			String text = jsonResponse.path("choices").get(0).path("message").path("content").asText().split("Question:")[1].replace("\n", " ").replace("  ", " ");
			String[] res = text.split(" Choices: a\\) ");
			String question = res[0].replace("Question: ", "");
			String[] choices = res[1].replace(",","").split(" [b-d]\\) ");
			return new QuestionResponse(word.getWid(), question, Arrays.stream(choices).toList());
		} catch (Exception e) {
			return getWordDefault(word);
		}
	}
	private static String escapeJson(String input) {
		return input.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
	}
	@Override
	public List<Word> findAll(){
		return wordRepository.findAll();
	}

	@Override
	public Boolean checkAnswer(Integer wid, String w, String uid) {
		Word word = wordRepository.findFirstByWid(wid);
		if(word == null) return null;
		History history = historyRepository.findFirstByUidAndWord(uid, word);
		if(!word.getWord().equals(w)){
			if(history == null){
				historyRepository.save(new History(0, uid, word, 0));
			}
			return false;
		}
		if(history == null){
			historyRepository.save(new History(0, uid, word, 1));
		}else{
			history.setIscorrect(1);
			historyRepository.save(history);
		}
		return true;
	}

	@Override
	public Word findFirstByWid(Integer wid) {
		return wordRepository.findFirstByWid(wid);
	}
}
