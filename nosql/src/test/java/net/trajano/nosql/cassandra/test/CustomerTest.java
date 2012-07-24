package net.trajano.nosql.cassandra.test;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.trajano.nosql.Customer;
import net.trajano.nosql.Customers;
import net.trajano.nosql.internal.CassandraCustomers;

import org.cassandraunit.AbstractCassandraUnit4TestCase;
import org.cassandraunit.dataset.DataSet;
import org.cassandraunit.dataset.json.ClassPathJsonDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CustomerTest extends AbstractCassandraUnit4TestCase {
	private Customers customers;

	@Override
	public DataSet getDataSet() {
		return new ClassPathJsonDataSet("customerDataSet.json");
	}

	@Before
	public void setUp() throws Exception {
		customers = new CassandraCustomers(getKeyspace());
	}

	@After
	public void tearDown() throws Exception {
		EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
	}

	@Test
	public void testCustomerInsertSearch() {
		final Customer customer = new Customer();
		customer.setName("Archimedes Trajano");
		customer.setLastRecallTimestamp(new Date());
		customer.setUuid(UUID.randomUUID());

		customers.add(customer);

		{
			final List<Customer> customerList = customers
					.find("Archimedes Trajano");
			assertEquals(1, customerList.size());
			assertEquals("Archimedes Trajano", customerList.get(0).getName());
		}
		{
			final List<Customer> customerList = customers
					.find("archimedes trajano");
			assertEquals(1, customerList.size());
			assertEquals("Archimedes Trajano", customerList.get(0).getName());
		}
		{
			final List<Customer> customerList = customers
					.find("archimedes TRAJANO");
			assertEquals(1, customerList.size());
			assertEquals("Archimedes Trajano", customerList.get(0).getName());
		}
		{
			final List<Customer> customerList = customers.find("archimedes");
			assertEquals(1, customerList.size());
			assertEquals("Archimedes Trajano", customerList.get(0).getName());
		}
		{
			final List<Customer> customerList = customers.find("archie");
			assertEquals(1, customerList.size());
			assertEquals("Archie", customerList.get(0).getName());
		}
		{
			final List<Customer> customerList = customers.find("archi");
			assertEquals(2, customerList.size());
		}
	}

	@Test
	public void testExistingDataSearch() {
		{
			final List<Customer> customerList = customers.find("archie");
			assertEquals(1, customerList.size());
			assertEquals("Archie", customerList.get(0).getName());
		}
	}

	@Test
	public void testMultipleInserts() {
		{
			final Customer customer = new Customer();
			customer.setName("Excel");
			customer.setLastRecallTimestamp(new Date());
			customer.setUuid(UUID.randomUUID());

			customers.add(customer);
		}
		{
			final Customer customer = new Customer();
			customer.setName("Hyatt");
			customer.setLastRecallTimestamp(new Date());
			customer.setUuid(UUID.randomUUID());

			customers.add(customer);
		}
		{
			final Customer customer = new Customer();
			customer.setName("Hayate");
			customer.setLastRecallTimestamp(new Date());
			customer.setUuid(UUID.randomUUID());

			customers.add(customer);
		}
		{
			final Customer customer = new Customer();
			customer.setName("Hinagiku");
			customer.setLastRecallTimestamp(new Date());
			customer.setUuid(UUID.randomUUID());

			customers.add(customer);
		}
		{
			final List<Customer> customerList = customers
					.find("Archimedes Trajano");
			assertEquals(0, customerList.size());
		}
		{
			final List<Customer> customerList = customers.find("e");
			assertEquals(1, customerList.size());
			assertEquals("Excel", customerList.get(0).getName());
		}
		{
			final List<Customer> customerList = customers.find("H");
			assertEquals(3, customerList.size());
		}
		{
			final List<Customer> customerList = customers.find("hayate");
			assertEquals(1, customerList.size());
			assertEquals("Hayate", customerList.get(0).getName());
		}
	}
}