package com.pa.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import com.pa.database.util.HibernateUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class RelatorioManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private final String ENDERECO_RELATORIOS_LATTES = "/iReportLattes.jrxml";
	private final String ENDERECO_RELATORIOS_LATTES_ORIENTATIONS_MESTRADO_ANDAMENTO = "/iReportLattesOrientationsAndamentoMestrado.jrxml";
	private final String ENDERECO_RELATORIOS_LATTES_ORIENTATIONS_MESTRADO_CONCLUIDA = "/iReportLattesOrientationsConcluidasMestrado.jrxml";
	private final String ENDERECO_RELATORIOS_LATTES_ORIENTATIONS_DOUTORADO_ANDAMENTO = "/iReportLattesOrientationsAndamentoDoutorado.jrxml";
	private final String ENDERECO_RELATORIOS_LATTES_ORIENTATIONS_DOUTORADO_CONCLUIDA = "/iReportLattesOrientationsConcluidasDoutorado.jrxml";
	private final String ENDERECO_RELATORIOS_LATTES_ORIENTATIONS_INICIACAO_CIENTIFICA = "/iReportLattesOrientationsIniciacaoCientifica.jrxml";
	private final String ENDERECO_RELATORIOS_LATTES_CURRICULOS = "/iReportLattesCurriculos.jrxml";
	private final String ENDERECO_RELATORIOS_LATTES_LIVROS = "/iReportLattesLivros.jrxml";
	private final String ENDERECO_RELATORIOS_LATTES_CAPITULOS = "/iReportLattesChapter.jrxml";
	private final String ENDERECO_RELATORIOS_LATTES_CONFERENCIAS = "/iReportLattesConferencias.jrxml";
	private final String ENDERECO_RELATORIOS_LATTES_PERIODICOS = "/iReportLattesPeriodicos.jrxml";
	private final String ENDERECO_RELATORIOS_LATTES_PATENTES = "/iReportLattesPatentes.jrxml";
	private final String ENDERECO_DIRETORIO_RELATORIOS = System.getProperty("user.home") + "//";
	private final String NOME_DO_ARQUIVO = "RELATORIO_LATTES.html";
	private final String NOME_DO_ARQUIVO_LIVROS = "RELATORIO_LATTES_LIVROS.html";
	private final String NOME_DO_ARQUIVO_CAPITULOS = "RELATORIO_LATTES_CAPITULOS.html";
	private final String NOME_DO_ARQUIVO_CONFERENCIAS = "RELATORIO_LATTES_CONFERENCIAS.html";
	private final String NOME_DO_ARQUIVO_PERIODICOS = "RELATORIO_LATTES_PERIODICOS.html";
	private final String NOME_DO_ARQUIVO_ORIENTATIONS_DOUTORADO_ANDAMENTO = "RELATORIO_LATTES_ORIENTATIONS_DOUTORADO_ANDAMENTO.html";
	private final String NOME_DO_ARQUIVO_CURRICULOS = "RELATORIO_LATTES_CURRICULOS.html";
	private final String NOME_DO_ARQUIVO_ORIENTATIONS_DOUTORADO_CONCLUIDO = "RELATORIO_LATTES_ORIENTATIONS_DOUTORADO_CONCLUIDO.html";
	private final String NOME_DO_ARQUIVO_ORIENTATIONS_MESTRADO_ANDAMENTO = "RELATORIO_LATTES_ORIENTATIONS_MESTRADO_ANDAMENTO.html";
	private final String NOME_DO_ARQUIVO_ORIENTATIONS_MESTRADO_CONCLUIDA = "RELATORIO_LATTES_ORIENTATIONS_MESTRADO_CONCLUIDO.html";
	private final String NOME_DO_ARQUIVO_ORIENTATIONS_INICIACAO_CIENTIFICA = "RELATORIO_LATTES_ORIENTATIONS_INICIACAO_CIENTIFICA.html";
	private final String NOME_DO_ARQUIVO_PATENTES = "RELATORIO_LATTES_PATENTES.html";

	private InputStream inputStream;
	private JasperDesign designInputStream;
	private JasperReport pathjrxml;
	private JasperPrint printReport;
	private Calendar dataAtual;
	private HashMap<String, String> parametros;

	public RelatorioManager() {
		this.dataAtual = Calendar.getInstance();
	}

	private HashMap<String, String> parametrizarConsulta(String dataDeInicioParametter, String dataDeFimParametter)
			throws IOException {
		this.parametros = new HashMap<String, String>();
		if (dataDeInicioParametter != null && dataDeFimParametter != null) {
			String dataInicio = dataDeInicioParametter;
			String dataFim = dataDeFimParametter;
			this.parametros.put("parametroDataInicio", dataInicio);
			this.parametros.put("parametroDataFim", dataFim);
		}
		return parametros;
	}

	private void compilarRelatorio(String endereco, HashMap parametros) throws JRException, SQLException {
		this.inputStream = getClass().getResourceAsStream(endereco);
		this.designInputStream = JRXmlLoader.load(inputStream);
		this.pathjrxml = JasperCompileManager.compileReport(designInputStream);
		Connection conection = HibernateUtil.getSessionFactory().getSessionFactoryOptions().getServiceRegistry()
				.getService(ConnectionProvider.class).getConnection();
		this.printReport = JasperFillManager.fillReport(this.pathjrxml, parametros, conection);

	}

	private void gerarHtmlDoRelatorio(String endereco, String nomeDoArquivo) throws JRException {
		String enderecoFinal = endereco + nomeDoArquivo;
		JasperExportManager.exportReportToHtmlFile(this.printReport, enderecoFinal);
	}

	public void gerarRelatorioLattes(String dataDeInicioParametter, String dataDeFimParametter)
			throws JRException, SQLException, IOException {
		this.compilarRelatorio(ENDERECO_RELATORIOS_LATTES,this.parametrizarConsulta(dataDeInicioParametter, dataDeFimParametter));
		this.gerarHtmlDoRelatorio(ENDERECO_DIRETORIO_RELATORIOS, NOME_DO_ARQUIVO);
		
		this.compilarRelatorio(ENDERECO_RELATORIOS_LATTES_ORIENTATIONS_DOUTORADO_ANDAMENTO,this.parametrizarConsulta(dataDeInicioParametter, dataDeFimParametter));
		this.gerarHtmlDoRelatorio(ENDERECO_DIRETORIO_RELATORIOS, NOME_DO_ARQUIVO_ORIENTATIONS_DOUTORADO_ANDAMENTO);
		
		this.compilarRelatorio(ENDERECO_RELATORIOS_LATTES_ORIENTATIONS_DOUTORADO_CONCLUIDA,this.parametrizarConsulta(dataDeInicioParametter, dataDeFimParametter));
		this.gerarHtmlDoRelatorio(ENDERECO_DIRETORIO_RELATORIOS, NOME_DO_ARQUIVO_ORIENTATIONS_DOUTORADO_CONCLUIDO);
		
		this.compilarRelatorio(ENDERECO_RELATORIOS_LATTES_ORIENTATIONS_MESTRADO_ANDAMENTO,this.parametrizarConsulta(dataDeInicioParametter, dataDeFimParametter));
		this.gerarHtmlDoRelatorio(ENDERECO_DIRETORIO_RELATORIOS, NOME_DO_ARQUIVO_ORIENTATIONS_MESTRADO_ANDAMENTO);
		
		this.compilarRelatorio(ENDERECO_RELATORIOS_LATTES_ORIENTATIONS_MESTRADO_CONCLUIDA,this.parametrizarConsulta(dataDeInicioParametter, dataDeFimParametter));
		this.gerarHtmlDoRelatorio(ENDERECO_DIRETORIO_RELATORIOS, NOME_DO_ARQUIVO_ORIENTATIONS_MESTRADO_CONCLUIDA);
		
		this.compilarRelatorio(ENDERECO_RELATORIOS_LATTES_ORIENTATIONS_INICIACAO_CIENTIFICA,this.parametrizarConsulta(dataDeInicioParametter, dataDeFimParametter));
		this.gerarHtmlDoRelatorio(ENDERECO_DIRETORIO_RELATORIOS, NOME_DO_ARQUIVO_ORIENTATIONS_INICIACAO_CIENTIFICA);
		
		this.compilarRelatorio(ENDERECO_RELATORIOS_LATTES_LIVROS,this.parametrizarConsulta(dataDeInicioParametter, dataDeFimParametter));
		this.gerarHtmlDoRelatorio(ENDERECO_DIRETORIO_RELATORIOS, NOME_DO_ARQUIVO_LIVROS);
		
		this.compilarRelatorio(ENDERECO_RELATORIOS_LATTES_CAPITULOS,this.parametrizarConsulta(dataDeInicioParametter, dataDeFimParametter));
		this.gerarHtmlDoRelatorio(ENDERECO_DIRETORIO_RELATORIOS, NOME_DO_ARQUIVO_CAPITULOS);
		
		this.compilarRelatorio(ENDERECO_RELATORIOS_LATTES_CONFERENCIAS,this.parametrizarConsulta(dataDeInicioParametter, dataDeFimParametter));
		this.gerarHtmlDoRelatorio(ENDERECO_DIRETORIO_RELATORIOS, NOME_DO_ARQUIVO_CONFERENCIAS);
		
		this.compilarRelatorio(ENDERECO_RELATORIOS_LATTES_PERIODICOS,this.parametrizarConsulta(dataDeInicioParametter, dataDeFimParametter));
		this.gerarHtmlDoRelatorio(ENDERECO_DIRETORIO_RELATORIOS, NOME_DO_ARQUIVO_PERIODICOS);
		
		this.compilarRelatorio(ENDERECO_RELATORIOS_LATTES_CURRICULOS,this.parametrizarConsulta(dataDeInicioParametter, dataDeFimParametter));
		this.gerarHtmlDoRelatorio(ENDERECO_DIRETORIO_RELATORIOS, NOME_DO_ARQUIVO_CURRICULOS);
		
		this.compilarRelatorio(ENDERECO_RELATORIOS_LATTES_PATENTES,this.parametrizarConsulta(dataDeInicioParametter, dataDeFimParametter));
		this.gerarHtmlDoRelatorio(ENDERECO_DIRETORIO_RELATORIOS, NOME_DO_ARQUIVO_PATENTES);
	}

}
