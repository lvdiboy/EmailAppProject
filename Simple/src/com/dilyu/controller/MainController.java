package com.dilyu.controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import com.dilyu.controller.services.CreateAndRegisterEmailAccountService;
import com.dilyu.controller.services.FolderUpdaterService;
import com.dilyu.controller.services.MessageRendererService;
import com.dilyu.controller.services.SaveAttachmentsService;
import com.dilyu.model.EmailMessageBean;
import com.dilyu.model.folder.EmailFolderBean;
import com.dilyu.model.table.BoldableRowFactory;
import com.dilyu.model.table.FormatableInteger;
import com.dilyu.view.ViewFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class MainController extends AbstractController implements Initializable{
	
    public MainController(ModelAccess modelAccess) {
		super(modelAccess);
	}
    
    

	@FXML
    private TreeView<String> emailFoldersTreeView;
    private MenuItem showDetails = new MenuItem("show details");

    
    @FXML
    private Label downAttachLabel;

    @FXML
    private ProgressBar downAttachProgress;
	
    private SaveAttachmentsService saveAttachmentsService;
    @FXML
    private TableView<EmailMessageBean> emailTableView;
	
    @FXML
    private TableColumn<EmailMessageBean, String> subjectCol;

    @FXML
    private TableColumn<EmailMessageBean, String> senderCol;

    @FXML
    private TableColumn<EmailMessageBean, FormatableInteger> sizeCol;
    
    @FXML
    private TableColumn<EmailMessageBean, Date> dateCol;
    
    @FXML
    private WebView messageRenderer;
	
    @FXML
    private Button Button1,downAttachBtn;
    private MessageRendererService messageRendererService;

    @FXML
    void Button1Action(ActionEvent event) {
    	Scene scene = ViewFactory.defaultFactory.getComposeMessageScene();
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
    }
    
    @FXML
    void downAttachBtnAction(ActionEvent event) {
    	EmailMessageBean message = emailTableView.getSelectionModel().getSelectedItem();
    	if(message != null  && message.hasAttachments()){
    		saveAttachmentsService.setMessageToDownload(message);
    		saveAttachmentsService.restart();
    	}
    }
    
    @FXML
    void changeReadAction() {
    	EmailMessageBean message = getModelAccess().getSelectedMessage();
    	if(message != null){
    		boolean value = message.isRead();
    		message.setRead(!value);
    		EmailFolderBean<String> selectedFolder= getModelAccess().getSelectedFolder();
    		if(selectedFolder != null){
    		if(value){
    			selectedFolder.incrementUnreadMessagesCount(1);
    		}else{
    			selectedFolder.decrementUnreadMessagesCount();
    		}
    		}
    	}
    }
    

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		downAttachProgress.setVisible(false);
	
		downAttachLabel.setVisible(false);
		saveAttachmentsService = new SaveAttachmentsService(downAttachProgress,downAttachLabel);
	    messageRendererService = new MessageRendererService(messageRenderer.getEngine());
		downAttachProgress.progressProperty().bind(saveAttachmentsService.progressProperty());
		
		
		FolderUpdaterService folderUpdaterService = new FolderUpdaterService(getModelAccess().getFoldersList());
		folderUpdaterService.start();
		
		emailTableView.setRowFactory(e-> new BoldableRowFactory<>());
		ViewFactory viewfactory = ViewFactory.defaultFactory;
		subjectCol.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, String>("subject"));
		senderCol.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, String>("sender"));
		dateCol.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, Date>("date"));
		sizeCol.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, FormatableInteger>("size"));		

		//BUG : size doesn't get it's default comparator overridden, so we do iy by hand!!!
		sizeCol.setComparator(new FormatableInteger(0));
		
		EmailFolderBean<String> root = new EmailFolderBean<>("");
		emailFoldersTreeView.setRoot(root);
		emailFoldersTreeView.setShowRoot(false);
		
		CreateAndRegisterEmailAccountService createAndRegisterEmailAccountService = 
				new CreateAndRegisterEmailAccountService("",
						"",
						root,
						getModelAccess());
		createAndRegisterEmailAccountService.start();
		
		emailTableView.setContextMenu(new ContextMenu(showDetails));
		
		emailFoldersTreeView.setOnMouseClicked(e ->{
			EmailFolderBean<String> item = (EmailFolderBean<String>)emailFoldersTreeView.getSelectionModel().getSelectedItem();
			if(item != null && !item.isTopElement()){
				emailTableView.setItems(item.getData());
				getModelAccess().setSelectedFolder(item);
				//clear the selected message:
				getModelAccess().setSelectedMessage(null);
			}
		});
		emailTableView.setOnMouseClicked(e->{
			EmailMessageBean message = emailTableView.getSelectionModel().getSelectedItem();
			if(message != null){
				getModelAccess().setSelectedMessage(message);
				
				messageRendererService.setMessageToRender(message);
				messageRendererService.restart();
				//messageRenderer.getEngine().loadContent(message.getContent());
			}
		});
		showDetails.setOnAction(e->{
			
			Scene scene = viewfactory.getEmailDetailsScene();
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.show();
		});		
		
		
	}
	

}
