package ru.imagebook.server.service.editor;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.editor.Layout;
import ru.imagebook.shared.model.editor.Page;
import ru.minogin.core.client.file.FileBean;

public interface EditorService {
	FileBean loadFolders(int userId);

	List<String> loadPreviews(int userId, String path);

	void showPreview(int userId, String path, OutputStream outputStream);

	Order<?> createOrder(String sessionId, int userId, int productId, int pageCount);

	List<Order<?>> loadOrders(int userId);

	void createFolder(int userId, String path, String name);

	void deleteFolder(int userId, String path);

	void editFolder(int userId, String path, String name);

	Map<Integer, List<Product>> loadProductsMap(int userId);

	void startAsync();

	void deleteImage(int userId, String path);

	List<Order<?>> loadAllOrders(int userId);

	/**
	 * Метод размещает на листе всю папку с изображениями и делает из простого заказа  - пакетный, а из текущего листа - индивидуальный
	 * @param sessionId - идентификатор сессии
	 * @param userId - идентификатор пользователя
	 * @param path - путь к папке с изображениями
	 * @param layoutType - тип размещения изображения на странице
	 * @param pageNumber - номер страницы
	 * */
	Order<?> addFolderWithImages(String sessionId, int userId,
										   String path, int layoutType, int pageNumber);

	/**Фиксируем текущий вариант слоя, с который работают на данный момент*/
	void changeCurrentOrderLayout(final String sessionId, final int userId, final Layout layout);

	/**Меняем для текущего листа тип с общего на индивидуальный и копируем его изображение на все слои того же листа*/
	Order<?> changePageTypeToIndividual(final String sessionId, final int userId, final Page page, final String path, final int pageNumber);

	Map<Integer, Page> addImage(String sessionId, int userId, String relativePath, int layoutType,
			int pageNumber);

	Order<?> copyOrder(String sessionId, int userId, int orderId);

	Order<?> openOrder(String sessionId, int userId, int orderId);

	void showComponent(String sessionId, int userId, int componentId, OutputStream outputStream);

	void processOrder(String sessionId, int userId);

	Page clearSpread(String sessionId, int userId, int pageNumber);

	void removeOrderFromSession(String sessionId);

	Order<?> dispose(String sessionId, int userId, String path);

	Order<?> changePageCount(String sessionId, int userId, int pageCount);

	void upload(HttpServletRequest request, String sessionId, int userId);

	Order<?> loadingOrderWithLayoutsAndPages(String sessionId, int userId);

	boolean requiredShowNotification(String sessionId, int userId, int typeId, boolean checkOnlyForCommonOrder);

	void cancelShowNotificationMessage(int userId, int typeId);
}
