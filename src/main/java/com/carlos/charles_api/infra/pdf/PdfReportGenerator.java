package com.carlos.charles_api.infra.pdf;

import com.carlos.charles_api.model.entity.ServiceOrder;
import com.carlos.charles_api.model.entity.SoState;
import com.carlos.charles_api.model.enums.ReportType;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PdfReportGenerator {

    public byte[] generateDetailedOsReport(ServiceOrder so) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 50, 50, 50, 50);

        try {
            PdfWriter.getInstance(doc, out);
            doc.open();

            // Cores
            Color borderColor = new Color(0, 0, 0);
            Color headerColor = new Color(230, 230, 250); // Lavanda 😌

            // Fontes
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font sectionFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font textFont = new Font(Font.HELVETICA, 12);
            Font tableHeaderFont = new Font(Font.HELVETICA, 12, Font.BOLD);

            // Título centralizado
            Paragraph title = new Paragraph("Relatório da Ordem de Serviço", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            doc.add(title);

            // Tabela de informações principais
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(20);
            infoTable.setWidths(new int[]{1, 2}); // Largura das colunas

            addRow(infoTable, "Código", so.getSoCode(), tableHeaderFont, textFont, borderColor, headerColor);
            addRow(infoTable, "Descrição", so.getDescription(), tableHeaderFont, textFont, borderColor, null);
            addRow(infoTable, "Diagnóstico", so.getDiagnostic(), tableHeaderFont, textFont, borderColor, null);
            addRow(infoTable, "Último estado", so.getCurrentState().name(), tableHeaderFont, textFont, borderColor, null);
            addRow(infoTable, "Solicitante", so.getSolicitant().getFullName(), tableHeaderFont, textFont, borderColor, null);
            addRow(infoTable, "Responsável",
                    so.getAssignee() != null ? so.getAssignee().getFullName() : "Não atribuído",
                    tableHeaderFont, textFont, borderColor, null);

            doc.add(infoTable);

            // Seção de histórico
            Paragraph historyHeader = new Paragraph("Histórico de Estados", sectionFont);
            historyHeader.setSpacingAfter(10);
            doc.add(historyHeader);

            PdfPTable historyTable = new PdfPTable(2);
            historyTable.setWidthPercentage(100);
            historyTable.setWidths(new int[]{1, 3});

            addHeaderCell(historyTable, "Data e Hora", tableHeaderFont, borderColor, headerColor);
            addHeaderCell(historyTable, "Estado", tableHeaderFont, borderColor, headerColor);

            List<SoState> states = so.getStates();
            for (SoState s : states) {
                historyTable.addCell(createCell(
                        s.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        textFont, borderColor));
                historyTable.addCell(createCell(
                        s.getType().name(), textFont, borderColor));
            }

            doc.add(historyTable);

        } catch (Exception e) {
            e.printStackTrace(); // Ou loga direito 🥲
        } finally {
            doc.close();
        }

        return out.toByteArray();
    }

    public byte[] generateOsListReport(List<ServiceOrder> orders, ReportType reportType) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4.rotate(), 50, 50, 50, 50);

        try {
            PdfWriter.getInstance(doc, out);
            doc.open();

            // Cores e Fontes
            Color borderColor = new Color(0, 0, 0);
            Color headerColor = new Color(230, 230, 250);
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font sectionFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font textFont = new Font(Font.HELVETICA, 12);
            Font tableHeaderFont = new Font(Font.HELVETICA, 12, Font.BOLD);

            // Título
            Paragraph title = new Paragraph("Relatório de Ordens de Serviço", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            doc.add(title);

            switch (reportType) {
                case ALL:
                    generateCompleteTable(doc, orders, borderColor, headerColor, tableHeaderFont, textFont);
                    break;
                case BY_STATE:
                    generateGroupedByStateTable(doc, orders, borderColor, headerColor, sectionFont, tableHeaderFont, textFont);
                    break;
                case BY_SOLICITANT:
                    generateGroupedBySolicitantTable(doc, orders, borderColor, headerColor, sectionFont, tableHeaderFont, textFont);
                    break;
                case BY_ASSIGNEE:
                    generateGroupedByAssigneeTable(doc, orders, borderColor, headerColor, sectionFont, tableHeaderFont, textFont);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            doc.close();
        }

        return out.toByteArray();
    }

    private void generateCompleteTable(Document doc, List<ServiceOrder> orders, Color borderColor, Color headerColor,
                                       Font tableHeaderFont, Font textFont) throws DocumentException {
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);

        String[] headers = {"Código", "Descrição", "Estado", "Solicitante", "Responsável", "Data de criação"};
        for (String header : headers) {
            addHeaderCell(table, header, tableHeaderFont, borderColor, headerColor);
        }

        for (ServiceOrder so : orders) {
            table.addCell(createCell(so.getSoCode(), textFont, borderColor));
            table.addCell(createCell(so.getDescription(), textFont, borderColor));
            table.addCell(createCell(so.getCurrentState().name(), textFont, borderColor));
            table.addCell(createCell(so.getSolicitant().getFullName(), textFont, borderColor));
            table.addCell(createCell(so.getAssignee() != null ? so.getAssignee().getFullName() : "Não atribuído", textFont, borderColor));
            table.addCell(createCell(so.getStates().getFirst().getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), textFont, borderColor));
        }

        doc.add(table);
    }

    private void generateGroupedByStateTable(Document doc, List<ServiceOrder> orders, Color borderColor, Color headerColor,
                                             Font sectionFont, Font tableHeaderFont, Font textFont) throws DocumentException {
        Map<String, List<ServiceOrder>> grouped = orders.stream()
                .collect(Collectors.groupingBy(so -> so.getCurrentState().name()));

        for (Map.Entry<String, List<ServiceOrder>> entry : grouped.entrySet()) {
            Paragraph stateHeader = new Paragraph(entry.getKey(), sectionFont);
            stateHeader.setSpacingBefore(15);
            stateHeader.setSpacingAfter(10);
            doc.add(stateHeader);

            generateCompleteTable(doc, entry.getValue(), borderColor, headerColor, tableHeaderFont, textFont);
        }
    }

    private void generateGroupedBySolicitantTable(Document doc, List<ServiceOrder> orders, Color borderColor, Color headerColor,
                                                  Font sectionFont, Font tableHeaderFont, Font textFont) throws DocumentException {
        Map<String, List<ServiceOrder>> grouped = orders.stream()
                .collect(Collectors.groupingBy(so -> so.getSolicitant().getFullName()));

        for (Map.Entry<String, List<ServiceOrder>> entry : grouped.entrySet()) {
            Paragraph solicitantHeader = new Paragraph("Solicitante: " + entry.getKey(), sectionFont);
            solicitantHeader.setSpacingBefore(15);
            solicitantHeader.setSpacingAfter(10);
            doc.add(solicitantHeader);

            generateCompleteTable(doc, entry.getValue(), borderColor, headerColor, tableHeaderFont, textFont);
        }
    }

    private void generateGroupedByAssigneeTable(Document doc, List<ServiceOrder> orders, Color borderColor, Color headerColor,
                                                Font sectionFont, Font tableHeaderFont, Font textFont) throws DocumentException {
        Map<String, List<ServiceOrder>> grouped = orders.stream()
                .collect(Collectors.groupingBy(so ->
                        so.getAssignee() != null ? so.getAssignee().getFullName() : "Não atribuído"));

        for (Map.Entry<String, List<ServiceOrder>> entry : grouped.entrySet()) {
            Paragraph assigneeHeader = new Paragraph("Responsável: " + entry.getKey(), sectionFont);
            assigneeHeader.setSpacingBefore(15);
            assigneeHeader.setSpacingAfter(10);
            doc.add(assigneeHeader);

            generateCompleteTable(doc, entry.getValue(), borderColor, headerColor, tableHeaderFont, textFont);
        }
    }

    

    private void addRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont, Color border, Color background) {
        PdfPCell cell1 = new PdfPCell(new Phrase(label, labelFont));
        cell1.setBorderColor(border);
        if (background != null) cell1.setBackgroundColor(background);
        table.addCell(cell1);

        PdfPCell cell2 = new PdfPCell(new Phrase(value, valueFont));
        cell2.setBorderColor(border);
        table.addCell(cell2);
    }

    private void addHeaderCell(PdfPTable table, String text, Font font, Color border, Color background) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(background);
        cell.setBorderColor(border);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private PdfPCell createCell(String text, Font font, Color border) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorderColor(border);
        return cell;
    }
}
