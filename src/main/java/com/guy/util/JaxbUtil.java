package com.guy.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Jaxb2工具类
 * @author		zhuc
 * @create		2013-3-29 下午2:40:14
 */
public class JaxbUtil {

	/**
	 * JavaBean转换成xml
	 * 默认编码UTF-8
	 * @param obj
	 * @param writer
	 * @return 
	 */
	public static String convertToXml(Object obj) {
		return convertToXml(obj, "GB2312");
	}

	/**
	 * JavaBean转换成xml
	 * @param obj
	 * @param encoding 
	 * @return 
	 * @throws JAXBException 
	 */
	public static String convertToXml(Object obj, String encoding) {
		String result = null;
	 
		try {
			JAXBContext context = JAXBContext.newInstance(obj.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
			StringWriter writer = new StringWriter();
			writer.append("<?xml version=\"1.0\" encoding=\""+encoding+"\"?>\n");
			marshaller.marshal(obj, writer);
			result = writer.toString();
//			JAXBContext context = JAXBContext.newInstance(obj.getClass());
//		    XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
//			Marshaller marshaller = context.createMarshaller();
//			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//			marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
//			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(baos, (String) marshaller.getProperty(Marshaller.JAXB_ENCODING));
//            xmlStreamWriter.writeStartDocument((String) marshaller.getProperty(Marshaller.JAXB_ENCODING), "1.0");
//            marshaller.marshal(obj, xmlStreamWriter);
//            xmlStreamWriter.writeEndDocument();
//            xmlStreamWriter.close();
//            return new String(baos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * xml转换成JavaBean
	 * @param xml
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T converyToJavaBean(String xml, Class<T> c) {
		T t = null;
		try {
			JAXBContext context = JAXBContext.newInstance(c);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			t = (T) unmarshaller.unmarshal(new StringReader(xml));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return t;
	}
	
	/**
	 * 将文件装换为String
	 * @param path
	 * @return
	 */
	public static String converyToString(String path,String encoding){
		StringBuffer txtContent = new StringBuffer();
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path),encoding));
			String line = null;
			while ((line = br.readLine()) != null) {
				txtContent.append(line.trim());
			}
			br.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		System.out.println(txtContent);
		return txtContent.toString();
	}
}
