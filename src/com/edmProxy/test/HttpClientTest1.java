package com.edmProxy.test;

import java.io.BufferedReader;     
import java.io.IOException;   
import java.io.InputStreamReader;     
import org.apache.http.HttpEntity;     
import org.apache.http.HttpHost;     
import org.apache.http.HttpResponse;     
import org.apache.http.HttpStatus;   
import org.apache.http.auth.AuthScope;   
import org.apache.http.auth.UsernamePasswordCredentials;   
import org.apache.http.client.ClientProtocolException;   
import org.apache.http.client.CredentialsProvider;   
import org.apache.http.client.HttpClient;   
import org.apache.http.client.methods.HttpGet;     
import org.apache.http.conn.params.ConnRoutePNames;     
import org.apache.http.impl.client.BasicCredentialsProvider;   
import org.apache.http.impl.client.DefaultHttpClient;    
import org.apache.http.util.EntityUtils; 

public class HttpClientTest1 {
    
	  
	    /**  
	     * @param args  
	     * @throws IOException   
	     * @throws ClientProtocolException   
	     */  
	    public static void main(String[] args) throws ClientProtocolException, IOException {   
	        //ʵ����һ��HttpClient   
	        HttpClient httpClient = new DefaultHttpClient();     
	        //�趨Ŀ��վ��   
	        HttpHost httpHost = new HttpHost("www.baidu.com");     
	        //���ô������ ip/��������,�˿�   
	        HttpHost proxy = new HttpHost("110.4.12.170", 83);   
	        //ʵ������֤   
	        CredentialsProvider credsProvider = new BasicCredentialsProvider();   
	        //�趨��֤����   
	        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("user001", "123456");   
	        //������֤   
	        credsProvider.setCredentials(   
	            new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),    
	            creds);   
	        httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);   
	        ((DefaultHttpClient)httpClient).setCredentialsProvider(credsProvider);   
	              
	        // Ŀ���ַ     
	        HttpGet httpget = new HttpGet("/"); 
	        
	        // ִ��     
	        HttpResponse response = httpClient.execute(httpHost, httpget);
	        System.out.println(HttpStatus.SC_OK+":"+response.getStatusLine().getStatusCode());
	        if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode()){   
	            //����ɹ�   
	            //ȡ����������   
	            HttpEntity entity = response.getEntity();   
	            //��ʾ����   
	            if (entity != null) {   
	                // ��ʾ���   
	                   
	                System.out.println(EntityUtils.toString(entity,"utf-8"));   
	                   
	            }   
	            if (entity != null) {   
	                entity.consumeContent();   
	            }   
	        }   
	    }   
	}  

