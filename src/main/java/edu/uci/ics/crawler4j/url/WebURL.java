/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.uci.ics.crawler4j.url;

import java.io.Serializable;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

/**
 * 如果你需要给WebURL添加一些属性，比如锚的标签名是a,img还是iframe，除了要在WebURL里面添加外，
 * 也需要修改WebURLTupleBinding，否则不会被存入DB，线程取出的时候该属性就会为空！
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */

@Entity
public class WebURL implements Serializable {

	private static final long serialVersionUID = 1L;

	@PrimaryKey
	private String url;

	private int docid;
	private int parentDocid;
	private String parentUrl;
	private short depth;
	private String domain;
	private String subDomain;
	private String path;
	private String anchor;

    // TODO 未验证
    // 关于优先级，crawler4j有个小BUG，
    // 就是WebURL的priority属性默认就是最小0，
    // 这使得如果你想优先爬取某URL就不可能了，解决方法是在WebURL构造函数或setURL里为priority赋上默认值，
	private byte priority;

	/**
	 * Returns the unique document id assigned to this Url.
	 */
	public int getDocid() {
		return docid;
	}

	public void setDocid(int docid) {
		this.docid = docid;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		WebURL otherUrl = (WebURL) o;
		return url != null && url.equals(otherUrl.getURL());

	}

	@Override
	public String toString() {
		return url;
	}

	/**
	 * Returns the Url string
	 */
	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;

		int domainStartIdx = url.indexOf("//") + 2;
		int domainEndIdx = url.indexOf('/', domainStartIdx);
		domain = url.substring(domainStartIdx, domainEndIdx);
		subDomain = "";
		String[] parts = domain.split("\\.");
		if (parts.length > 2) {
			domain = parts[parts.length - 2] + "." + parts[parts.length - 1];
			int limit = 2;
			if (TLDList.contains(domain)) {
				domain = parts[parts.length - 3] + "." + domain;
				limit = 3;
			}
			for (int i = 0; i < parts.length - limit; i++) {
				if (subDomain.length() > 0) {
					subDomain += ".";
				}
				subDomain += parts[i];
			}
		}
		path = url.substring(domainEndIdx);
		int pathEndIdx = path.indexOf('?');
		if (pathEndIdx >= 0) {
			path = path.substring(0, pathEndIdx);
		}
	}

	/**
	 * Returns the unique document id of the parent page. The parent page is the
	 * page in which the Url of this page is first observed.
	 */
	public int getParentDocid() {
		return parentDocid;
	}

	public void setParentDocid(int parentDocid) {
		this.parentDocid = parentDocid;
	}

	/**
	 * Returns the url of the parent page. The parent page is the page in which
	 * the Url of this page is first observed.
	 */
	public String getParentUrl() {
		return parentUrl;
	}

	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
	}

	/**
	 * Returns the crawl depth at which this Url is first observed. Seed Urls
	 * are at depth 0. Urls that are extracted from seed Urls are at depth 1,
	 * etc.
	 */
	public short getDepth() {
		return depth;
	}

	public void setDepth(short depth) {
		this.depth = depth;
	}

	/**
	 * Returns the domain of this Url. For 'http://www.example.com/sample.htm',
	 * domain will be 'example.com'
	 */
	public String getDomain() {
		return domain;
	}

	public String getSubDomain() {
		return subDomain;
	}

	/**
	 * Returns the path of this Url. For 'http://www.example.com/sample.htm',
	 * domain will be 'sample.htm'
	 */
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Returns the anchor string. For example, in <a href="example.com">A sample anchor</a>
	 * the anchor string is 'A sample anchor'
	 */
	public String getAnchor() {
		return anchor;
	}
	
	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}

	/**
	 * Returns the priority for crawling this URL.
	 * A lower number results in higher priority.
	 */
	public byte getPriority() {
		return priority;
	}

	public void setPriority(byte priority) {
		this.priority = priority;
	}

	
}
