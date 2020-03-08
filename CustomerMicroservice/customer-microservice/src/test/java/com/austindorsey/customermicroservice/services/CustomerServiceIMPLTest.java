package com.austindorsey.customermicroservice.services;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;

import com.austindorsey.customermicroservice.model.Customer;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerServiceIMPLTest {
	@InjectMocks private CustomerService service = new CustomerServiceIMPL();
	@InjectMocks private DriverManagerWrapper driverManagerWrapper = Mockito.spy(DriverManagerWrapper.class);
	@Mock private Connection mockConnection;
	@Mock private Statement mockStatement;
	@Mock private ResultSet mockResult;

	@Test
	void getCustomers_3Customers() throws Exception  {
		Customer customer1 = new Customer(1, "John", "Doe", 1, Date.valueOf("2012-2-24"));
		Customer customer2 = new Customer(5, null, null, 0, Date.valueOf("2020-3-3"));
		Customer customer3 = new Customer(12, "Jane", "Doe", 8, Date.valueOf("2008-11-20"));
		Customer[] expectedCustomers = {customer1, customer2, customer3};

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(customer1.getId())
            .thenReturn(customer2.getId())
            .thenReturn(customer3.getId());
        when(mockResult.getString("firstName"))
            .thenReturn(customer1.getFirstName())
            .thenReturn(customer2.getFirstName())
            .thenReturn(customer3.getFirstName());
        when(mockResult.getString("lastName"))
            .thenReturn(customer1.getLastName())
            .thenReturn(customer2.getLastName())
            .thenReturn(customer3.getLastName());
        when(mockResult.getInt("numberOfOrders"))
            .thenReturn(customer1.getNumberOfOrders())
            .thenReturn(customer2.getNumberOfOrders())
            .thenReturn(customer3.getNumberOfOrders());
        when(mockResult.getDate("memberSince"))
            .thenReturn(customer1.getMemberSince())
            .thenReturn(customer2.getMemberSince())
            .thenReturn(customer3.getMemberSince());

		Customer[] returnedCustomers = service.getCustomers();

		assertArrayEquals(expectedCustomers, returnedCustomers);
		verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
	}

	@Test
	void getCustomers_noCustomers() throws Exception  {
		Customer[] expectedCustomers = {};

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

		Customer[] returnedCustomers = service.getCustomers();

		assertArrayEquals(expectedCustomers, returnedCustomers);
		verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
	}

	@Test
	void getCustomerByID_found() throws Exception  {
		Customer customer = new Customer(1, "John", "Doe", 1, Date.valueOf("2012-2-24"));
		
        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(customer.getId());
        when(mockResult.getString("firstName"))
            .thenReturn(customer.getFirstName());
        when(mockResult.getString("lastName"))
            .thenReturn(customer.getLastName());
        when(mockResult.getInt("numberOfOrders"))
            .thenReturn(customer.getNumberOfOrders());
        when(mockResult.getDate("memberSince"))
            .thenReturn(customer.getMemberSince());

		Customer returnedCustomers = service.getCustomerByID(customer.getId());

		assertEquals(customer, returnedCustomers);
		verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
	}

	@Test
	void getCustomerByID_notFound() throws Exception  {
		Customer customer = null;

		Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

		Customer returnedCustomers = service.getCustomerByID(1);

		assertEquals(customer, returnedCustomers);
		verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
	}

	@Test
	void getCustomersByFirstName_2Customers() throws Exception  {
		Customer customer1 = new Customer(1, "John", "Doe", 1, Date.valueOf("2012-2-24"));
		Customer customer2 = new Customer(7, "John", null, 0, Date.valueOf("2008-11-20"));
		Customer[] expectedCustomers = {customer1, customer2};

		Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(customer1.getId())
            .thenReturn(customer2.getId());
        when(mockResult.getString("firstName"))
            .thenReturn(customer1.getFirstName())
            .thenReturn(customer2.getFirstName());
        when(mockResult.getString("lastName"))
            .thenReturn(customer1.getLastName())
            .thenReturn(customer2.getLastName());
        when(mockResult.getInt("numberOfOrders"))
            .thenReturn(customer1.getNumberOfOrders())
            .thenReturn(customer2.getNumberOfOrders());
        when(mockResult.getDate("memberSince"))
            .thenReturn(customer1.getMemberSince())
            .thenReturn(customer2.getMemberSince());

		Customer[] returnedCustomers = service.getCustomersByName(customer1.getFirstName(), null);

		assertArrayEquals(expectedCustomers, returnedCustomers);
		verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    
    @Test
	void getCustomersByLastName_2Customers() throws Exception  {
		Customer customer1 = new Customer(1, "John", "Doe", 1, Date.valueOf("2012-2-24"));
		Customer customer2 = new Customer(7, "Jane", "Doe", 0, Date.valueOf("2008-11-20"));
		Customer[] expectedCustomers = {customer1, customer2};

		Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(customer1.getId())
            .thenReturn(customer2.getId());
        when(mockResult.getString("firstName"))
            .thenReturn(customer1.getFirstName())
            .thenReturn(customer2.getFirstName());
        when(mockResult.getString("lastName"))
            .thenReturn(customer1.getLastName())
            .thenReturn(customer2.getLastName());
        when(mockResult.getInt("numberOfOrders"))
            .thenReturn(customer1.getNumberOfOrders())
            .thenReturn(customer2.getNumberOfOrders());
        when(mockResult.getDate("memberSince"))
            .thenReturn(customer1.getMemberSince())
            .thenReturn(customer2.getMemberSince());

		Customer[] returnedCustomers = service.getCustomersByName(null, customer1.getLastName());

		assertArrayEquals(expectedCustomers, returnedCustomers);
		verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    
    @Test
	void getCustomersByFullName_2Customers() throws Exception  {
		Customer customer1 = new Customer(1, "John", "Doe", 1, Date.valueOf("2012-2-24"));
		Customer customer2 = new Customer(7, "John", "Doe", 0, Date.valueOf("2008-11-20"));
		Customer[] expectedCustomers = {customer1, customer2};

		Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(customer1.getId())
            .thenReturn(customer2.getId());
        when(mockResult.getString("firstName"))
            .thenReturn(customer1.getFirstName())
            .thenReturn(customer2.getFirstName());
        when(mockResult.getString("lastName"))
            .thenReturn(customer1.getLastName())
            .thenReturn(customer2.getLastName());
        when(mockResult.getInt("numberOfOrders"))
            .thenReturn(customer1.getNumberOfOrders())
            .thenReturn(customer2.getNumberOfOrders());
        when(mockResult.getDate("memberSince"))
            .thenReturn(customer1.getMemberSince())
            .thenReturn(customer2.getMemberSince());

		Customer[] returnedCustomers = service.getCustomersByName(customer1.getFirstName(), customer2.getLastName());

		assertArrayEquals(expectedCustomers, returnedCustomers);
		verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
	}

	@Test
	void getCustomersByFirstName_1Customer() throws Exception  {
		Customer customer = new Customer(1, "John", "Doe", 1, Date.valueOf("2012-2-24"));
		Customer[] expectedCustomers = {customer};

		Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(customer.getId());
        when(mockResult.getString("firstName"))
            .thenReturn(customer.getFirstName());
        when(mockResult.getString("lastName"))
            .thenReturn(customer.getLastName());
        when(mockResult.getInt("numberOfOrders"))
            .thenReturn(customer.getNumberOfOrders());
        when(mockResult.getDate("memberSince"))
            .thenReturn(customer.getMemberSince());

		Customer[] returnedCustomers = service.getCustomersByName(customer.getFirstName(), null);

		assertArrayEquals(expectedCustomers, returnedCustomers);
		verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
	}

	@Test
	void getCustomersByFirstName_noCustomer() throws Exception  {
		Customer[] expectedCustomers = {};

		Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

		Customer[] returnedCustomers = service.getCustomersByName("John", null);

		assertArrayEquals(expectedCustomers, returnedCustomers);
		verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    
	@Test
	void getCustomersByNullName_noCustomer() throws Exception  {
		Customer[] expectedCustomers = {};

		Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

		Customer[] returnedCustomers = service.getCustomersByName(null, null);

		assertArrayEquals(expectedCustomers, returnedCustomers);
		verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
	}

	@Test
	void incrementOrderCount_customerFound() throws Exception  {
		Customer customer = new Customer(1, "John", "Doe", 1, Date.valueOf("2012-2-24"));

		Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
		when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("numberOfOrders"))
            .thenReturn(customer.getNumberOfOrders());
		when(mockStatement.executeUpdate(anyString())).thenReturn(1);

		int returnedOrderCount = service.incrementOrderCount(customer.getId());

		assertEquals(customer.getNumberOfOrders() + 1, returnedOrderCount);
		verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
		verify(mockStatement, times(1)).executeQuery(anyString());
		verify(mockStatement, times(1)).executeUpdate(anyString());
	}
	
	@Test
	void incrementOrderCount_notFound() throws Exception  {
		Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
		when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

		int returnedOrderCount = service.incrementOrderCount(8);

		assertEquals(-1, returnedOrderCount);
		verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
		verify(mockStatement, times(1)).executeQuery(anyString());
	}

	@Test
	void changeName_firstAndLast() throws Exception  {
		Customer customer = new Customer(1, "John", "Doe", 1, Date.valueOf("2012-2-24"));
		String newFirst = "Johnathan";
		String newLast = "Doeman";
		String expectedFullName = newFirst + " " + newLast;

		Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeUpdate(anyString()))
            .thenReturn(1)
            .thenReturn(1);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(customer.getId());
        when(mockResult.getString("firstName"))
            .thenReturn(newFirst);
        when(mockResult.getString("lastName"))
            .thenReturn(newLast);
        when(mockResult.getInt("numberOfOrders"))
            .thenReturn(customer.getNumberOfOrders());
        when(mockResult.getDate("memberSince"))
			.thenReturn(customer.getMemberSince());

		String returnedNewFullName = service.changeName(customer.getId(), newFirst, newLast);

		assertEquals(expectedFullName, returnedNewFullName);
		verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
		verify(mockStatement, times(1)).executeQuery(anyString());
		verify(mockStatement, times(2)).executeUpdate(anyString());
	}

	@Test
	void changeName_firstOnly() throws Exception  {
		Customer customer = new Customer(1, "John", "Doe", 1, Date.valueOf("2012-2-24"));
		String newFirst = "Johnathan";
		String expectedFullName = newFirst + " " + customer.getLastName();

		Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeUpdate(anyString())).thenReturn(1);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(customer.getId());
        when(mockResult.getString("firstName"))
            .thenReturn(newFirst);
        when(mockResult.getString("lastName"))
            .thenReturn(customer.getLastName());
        when(mockResult.getInt("numberOfOrders"))
            .thenReturn(customer.getNumberOfOrders());
        when(mockResult.getDate("memberSince"))
			.thenReturn(customer.getMemberSince());

		String returnedNewFullName = service.changeName(customer.getId(), newFirst, null);

		assertEquals(expectedFullName, returnedNewFullName);
		verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
		verify(mockStatement, times(1)).executeQuery(anyString());
		verify(mockStatement, times(1)).executeUpdate(anyString());
	}
	
	@Test
	void changeName_lastOnly() throws Exception  {
		Customer customer = new Customer(1, "John", "Doe", 1, Date.valueOf("2012-2-24"));
		String newLast = "Doeman";
		String expectedFullName = customer.getFirstName() + " " + newLast;

		Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeUpdate(anyString())).thenReturn(1);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(customer.getId());
        when(mockResult.getString("firstName"))
            .thenReturn(customer.getFirstName());
        when(mockResult.getString("lastName"))
            .thenReturn(newLast);
        when(mockResult.getInt("numberOfOrders"))
            .thenReturn(customer.getNumberOfOrders());
        when(mockResult.getDate("memberSince"))
			.thenReturn(customer.getMemberSince());

		String returnedNewFullName = service.changeName(customer.getId(), null, newLast);

		assertEquals(expectedFullName, returnedNewFullName);
		verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
		verify(mockStatement, times(1)).executeQuery(anyString());
		verify(mockStatement, times(1)).executeUpdate(anyString());
	}
	
	@Test
	void changeName_noChange() throws Exception  {
		Customer customer = new Customer(1, "John", "Doe", 1, Date.valueOf("2012-2-24"));
		String expectedFullName = customer.getFirstName() + " " + customer.getLastName();

		Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(customer.getId());
        when(mockResult.getString("firstName"))
            .thenReturn(customer.getFirstName());
        when(mockResult.getString("lastName"))
            .thenReturn(customer.getLastName());
        when(mockResult.getInt("numberOfOrders"))
            .thenReturn(customer.getNumberOfOrders());
        when(mockResult.getDate("memberSince"))
			.thenReturn(customer.getMemberSince());

		String returnedNewFullName = service.changeName(customer.getId(), null, null);

		assertEquals(expectedFullName, returnedNewFullName);
		verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
	}
	
	@Test
	void changeName_customerNotFound() throws Exception  {
		String expectedFullName = null;

		Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

		String returnedNewFullName = service.changeName(14, null, null);

		assertEquals(expectedFullName, returnedNewFullName);
		verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
}
