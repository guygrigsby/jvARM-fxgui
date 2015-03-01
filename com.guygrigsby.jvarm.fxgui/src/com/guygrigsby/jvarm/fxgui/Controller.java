package com.guygrigsby.jvarm.fxgui;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.guygrigsby.jvarm.core.ArmProgram;
import com.guygrigsby.jvarm.core.ArmSourceCompiler;
import com.guygrigsby.jvarm.core.CompilerInfoCollector;

public class Controller {

	private static Logger logger = LogManager.getLogger();

	@FXML
	private TextArea editor;
	
	@FXML
	private TextArea console;

	@FXML
	private TableView<ObservableMap.Entry<String, Integer>> registersTable;

	@FXML
	private TableColumn<ObservableMap.Entry<String, Integer>, String> registerNameCol;
	@FXML
	private TableColumn<ObservableMap.Entry<String, Integer>, String> registerValueCol;
	@FXML
	private TitledPane registerPane;
	@FXML
	private Accordion rightAccordion;

	private ArmProgram program;

	private ObservableMap<String, Integer> registers;

	private Stage stage;

	@FXML
	private void initialize() {
		editor.setText("ADD r0, r0, #1     ; r0 = r0 + r1\nADD r1, r0, #4     ; r0 = r0 + 4\nADD r0, r0, r0, LSL r0");
		rightAccordion.setExpandedPane(registerPane);
	}

	@FXML
	public void step() {
		logger.trace("Step");
		program.step(registers);
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@FXML
	public void startDebug() {
		String sourceCode = editor.getText();
		InputStream is = new ByteArrayInputStream(
				sourceCode.getBytes(StandardCharsets.UTF_8));
		CompilerInfoCollector collector = new CompilerInfoCollector();
		program = new ArmSourceCompiler().compile(is, collector);

		if (collector.hasErrors()) {
			String errorString = collector.getErrorsAsString();
			console.appendText(errorString);
			return;
		} else {
			console.setText("");
		}
		createRegisters();

		registersTable.setEditable(true);

		registers
				.addListener((
						MapChangeListener.Change<? extends String, ? extends Integer> change) -> {
					registersTable.getItems().setAll(registers.entrySet());
				});
		registerNameCol.setComparator(new RegisterNameComparator());
		registerNameCol.setCellValueFactory((p) -> {
			return new SimpleStringProperty(p.getValue().getKey());
		});

		registerNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		registerNameCol.setEditable(false);
		registerValueCol.setCellValueFactory((p) -> {
			int value = p.getValue().getValue();
			String hexString = String.format("%#010x\n", value); //0x00000000
			return new SimpleStringProperty(hexString);
		});

		registerValueCol.setCellFactory(TextFieldTableCell.forTableColumn());

		registersTable.getItems().setAll(registers.entrySet());
	}

	@FXML
	public void loadFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open ARM Source File");
		File file = fileChooser.showOpenDialog(stage);
		Charset encoding = FileUtilities.getSystemCharset();
		String contents;
		try {
			contents = FileUtilities.readFile(file.getAbsolutePath(), encoding);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} 
		editor.setText(contents);
	}

	private void createRegisters() {
		registers = FXCollections.observableHashMap();
		registers.put("R0", 0);
		registers.put("R1", 0);
		registers.put("R2", 0);
		registers.put("R3", 0);
		registers.put("R4", 0);
		registers.put("R5", 0);
		registers.put("R6", 0);
		registers.put("R7", 0);
		registers.put("R8", 0);
		registers.put("R9", 0);
		registers.put("R10", 0);
		registers.put("R11", 0);
		registers.put("R10", 0);
		registers.put("R13", 0);
		registers.put("R14", 0);
		registers.put("R15", 0);
		registers.put("CPSR", 0);
	}

	public void setStageAndSetupListeners(Stage stageIn) {
		stage = stageIn;
	}
}
