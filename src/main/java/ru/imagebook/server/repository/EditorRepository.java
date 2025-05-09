package ru.imagebook.server.repository;

import java.util.List;

import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.editor.Component;
import ru.imagebook.shared.model.editor.Image;
import ru.imagebook.shared.model.editor.Layout;

public interface EditorRepository {
	Product getProduct(int productId);

	void saveOrder(AlbumOrder order);

	Color getColor(int number);

	int nextCounter();

	List<Order<?>> loadOrders(User user);

	Order<?> getOrder(int orderId);

	Order<?> loadOrderWithLayout(int orderId);

	Image getImage(int id);

	void flush();

	List<Product> loadProducts();

	List<Order<?>> loadProcessingOrders();

	List<Order<?>> loadAllOrders(User user);

	Component getComponent(int componentId);

	Album getAlbum(int type, int number);

	void saveLayout(Layout layout, int orderId);

	void deleteOrder(Order<?> order);

	/**Запрашиваем о необходимости получать сообщения на ГУИ
	 * @param userId Пользователь, для которого делается запрос
	 * @param notificationTypeId тип сообщения
	 * */
	boolean requiredShowNotificationMessage(Integer userId, int notificationTypeId);

	/**Отказ от получения нотификаций для определенного типа
	 * @param userId Пользователь, который отказывается от получения
	 * @param notificationTypeId тип нотификации
	 * */
	void cancelFromNotification(Integer userId, int notificationTypeId);
}
