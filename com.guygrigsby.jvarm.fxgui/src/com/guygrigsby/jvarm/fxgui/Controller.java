package com.guygrigsby.jvarm.fxgui;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

import javafx.application.Platform;
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
import com.guygrigsby.jvarm.core.InfoCollector;
import com.guygrigsby.jvarm.core.Registers;

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

	private Registers registers;

	private Stage stage;

	private File openFile;

	@FXML
	private void initialize() {
		URL fileUrl = this.getClass().getResource("testFile.s");
		String path = fileUrl.toString();
		File testFile = new File(path);
		//loadFile(testFile);
		editor.setStyle("-fx-highlight-fill: yellow; -fx-highlight-text-fill: black;");
		
		String testEditorContents = 
				"MOV r0, #0\n"
				+ "MOV r1, #1\n"
				+ "MOV r12, #10\n"
				+ "loop\n"
				+ "ADD r2, r1, r0\n"
				+ "MOV r0, r1\n"
				+ "MOV r1, r2\n"
				+ "SUB r12, r12, #1\n"
				+ "CMP r12, #0\n"
				+ "BNE loop";
		
		editor.setText(testEditorContents);
		rightAccordion.setExpandedPane(registerPane);
	}

	@FXML
	public void step() {
		try {
			logger.trace("Step: " + program.getInstructionUnderExecution());
			program.step(registers);
			int lineUnderExec = program.getLineUnderExecution();
			if (lineUnderExec != -1) {
				highlightEditorLine(lineUnderExec);
				registersTable.sort();
			} else {
				console.appendText("Execution Ended");
				Platform.runLater(() -> editor.selectRange(0, 0));
			}
		} catch (Exception e) {
			//meant to catch all so an error in the core does not crach the GUI
			logger.error(e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@FXML
	public void startDebug() {
		String sourceCode = editor.getText();
		InputStream is = new ByteArrayInputStream(
				sourceCode.getBytes(StandardCharsets.UTF_8));
		InfoCollector collector = new CompilerInfoCollector();
		program = new ArmSourceCompiler().compile(is, collector);

		if (collector.hasErrors()) {
			String errorString = collector.getErrorsAsString();
			console.appendText(errorString);
			return;
		} else {
			console.setText("");
		}
		Map<String, Integer> regMap = createRegistersMap();
		registers = new Registers(regMap);
		
		registersTable.setEditable(true);

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
		highlightEditorLine(program.getLineUnderExecution());
		registersTable.sort();
	}

	@FXML
	public void openFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open ARM Source File");
		openFile = fileChooser.showOpenDialog(stage);
		if (openFile != null) {
			loadFile(openFile);
		}
	}
	private void loadFile(File file) {
		Charset encoding = FileUtilities.getSystemCharset();
		String contents;
		try {
			contents = FileUtilities.readFile(file.getPath(), encoding);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} 
		editor.setText(contents);
	}
	@FXML
	public void saveFile() {
		FileUtilities.saveFile(editor.getText(), openFile);
	}

	private Map<String, Integer> createRegistersMap() {
		ObservableMap<String, Integer> registerMap = FXCollections.observableHashMap();
		registerMap
		.addListener((
				MapChangeListener.Change<? extends String, ? extends Integer> change) -> {
					if (registers != null) {
						Set<Map.Entry<String, Integer>> set = registers.entrySet();
						if (set != null) {
							registersTable.getItems().setAll(set);
						}
					}
		});
		return registerMap;
	}

	public void setStageAndSetupListeners(Stage stageIn) {
		stage = stageIn;
	}
	
	private void highlightEditorLine(int lineNo) {
		String text = editor.getText();
		int anchor = 0;
		int carat = 0;
		int newLineCount = 0;
		for (int k = 0; k < text.length(); k++) {
			char current = text.charAt(k);
			if (current == '\n' ) {
				newLineCount++;
				if ((lineNo - 1) == newLineCount) {
					anchor = k;
					break;
				}
			}
		}
		for (int k = anchor + 1; k < text.length(); k++) {
			char current = text.charAt(k);
			if (current == '\n') {
				carat = k;
				break;
			}
		}
		if (carat == 0) {
			carat = text.length() - 1;
		}
		final int finalAnchor = anchor;
		final int finalCarat = carat;
	    Platform.runLater(() -> editor.selectRange(finalAnchor, finalCarat));
	}
}
