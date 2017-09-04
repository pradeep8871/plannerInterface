package com.opta.demo.todo;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.opta.demo.appConfig.StaticField;

@Service
public class KieService {
	private static final Logger log = LoggerFactory.getLogger(KieService.class);
	

	public String makeKieContainer(String containerId) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpPut put = new HttpPut(StaticField.KIESERVERURL+containerId);
		Document doc = makeXmlforContainer(containerId);
		String xml = convertXMLToString(doc);
		HttpEntity entity = null;
		HttpResponse response = null;
		String result = "";
		put.setHeader("authorization", StaticField.AUTHORIZATION);
		put.setHeader("X-KIE-ContentType", "xstream");
		put.setHeader("content-type", "application/xml");
		try {
			entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
			put.setEntity(entity);
			response = client.execute(put);
			result = EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String makeKieServer(String containerId) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpPut put = new HttpPut(StaticField.KIESERVERURL+containerId+"/solvers/taskPlanningSolver");
		Document doc = makeXmlforServerConfig();
		String xml = convertXMLToString(doc);
		HttpEntity entity = null;
		HttpResponse response = null;
		String result = "";
		put.setHeader("authorization", StaticField.AUTHORIZATION);
		put.setHeader("X-KIE-ContentType", "xstream");
		put.setHeader("content-type", "application/xml");
		try {
			entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
			put.setEntity(entity);
			response = client.execute(put);
			result = EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String xmlSendToKieServer(String xml,String containerId) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(StaticField.KIESERVERURL+containerId+"/solvers/taskPlanningSolver/state/solving");
		HttpEntity entity = null;
		HttpResponse response = null;
		String result = "";
		post.setHeader("authorization", StaticField.AUTHORIZATION);
		post.setHeader("X-KIE-ContentType", "xstream");
		post.setHeader("content-type", "application/xml");
		try {
			entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
			post.setEntity(entity);
			response = client.execute(post);
			result = EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String getSolutionFromKieServer(String containerId) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(StaticField.KIESERVERURL+containerId+"/solvers/taskPlanningSolver/bestsolution");
		HttpEntity entity = null;
		HttpResponse response = null;
		String result = "";
		get.setHeader("authorization", StaticField.AUTHORIZATION);
		get.setHeader("X-KIE-ContentType", "xstream");
		get.setHeader("content-type", "application/xml");
		try {
			response = client.execute(get);
			result = EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void deleteKieContainer(String containerId) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpDelete delete = new HttpDelete(StaticField.KIESERVERURL+containerId);
		HttpEntity entity = null;
		HttpResponse response = null;
		String result = "";
		delete.setHeader("authorization", StaticField.AUTHORIZATION);
		delete.setHeader("X-KIE-ContentType", "xstream");
		delete.setHeader("content-type", "application/xml");
		try {
			response = client.execute(delete);
			result = EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("result "+result);
	}


	private String convertXMLToString(Document doc) {
		try {
			TransformerFactory tFact = TransformerFactory.newInstance();
			Transformer trans = tFact.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			DOMSource source = new DOMSource(doc);
			trans.transform(source, result);
			return writer.toString();
		} catch (TransformerException ex) {
			ex.getMessage();
		}
		return null;
	}

	private Document makeXmlforContainer(String containerId) {
		Document doc = getDocument();
		Element kieCont = doc.createElement("kie-container");
		kieCont.setAttribute("container-id", containerId);
		Element releaseId = doc.createElement("release-id");
		Element groupId = doc.createElement("group-id");
		groupId.appendChild(doc.createTextNode(StaticField.GROUPID));
		Element artifactId = doc.createElement("artifact-id");
		artifactId.appendChild(doc.createTextNode(StaticField.ARTIFACTID));
		Element version = doc.createElement("version");
		version.appendChild(doc.createTextNode(StaticField.VERSION));
		releaseId.appendChild(groupId);
		releaseId.appendChild(artifactId);
		releaseId.appendChild(version);
		kieCont.appendChild(releaseId);
		doc.appendChild(kieCont);
		return doc;
	}

	private Document makeXmlforServerConfig() {
		Document doc = getDocument();
		Element serverInstance = doc.createElement("solver-instance");
		Element serverConfigFile = doc.createElement("solver-config-file");
		serverConfigFile.appendChild(doc.createTextNode(StaticField.SERVERCONFIGFILE));
		serverInstance.appendChild(serverConfigFile);
		doc.appendChild(serverInstance);
		return doc;
	}

	private Document getDocument() {
		DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
		DocumentBuilder build = null;
		try {
			build = dFact.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document doc = build.newDocument();
		return doc;
	}

}
