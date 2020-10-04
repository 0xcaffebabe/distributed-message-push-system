import sys 
sys.path.append("..") 
from http_template import *
import unittest
import mock

class HttpTemplateTest(unittest.TestCase):

  @mock.patch('http_template.requests')
  def test_get(self, requests):
    
    requests.get.return_value = self
    self.text = 'test html'

    t = HttpTemplate()
    result = t.get('http://baidu.com')
    
    requests.get.assert_called_with('http://baidu.com')
    self.assertEqual('test html', result)
    ...

