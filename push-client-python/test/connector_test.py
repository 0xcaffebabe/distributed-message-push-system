
from connector import *
import unittest
import mock

class ConnectorTest(unittest.TestCase):

  def test_is_available_excepect_false(self):
    connector = Connector()
    self.assertEqual(False, connector.is_available())