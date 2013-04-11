/*
 *    Copyright 2013 Bryn Cooke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jglue.jsonfluent;

import java.io.IOException;
import java.io.Writer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;

/**
 * Factory for builders.
 * 
 * @author Bryn Cooke
 * 
 */
public class JsonBuilderFactory {

	/**
	 * @return Start building a new json object.
	 */
	public static JsonObjectBuilder<?, JsonObject> buildObject() {
		JsonObject o = new JsonObject();
		return new Impl(o, o);
	}
	


	/**
	 * @return Start building new json array.
	 */
	public static JsonArrayBuilder<?, JsonArray> buildArray() {
		JsonArray o = new JsonArray();
		return new Impl(o, o);
	}

	/**
	 * @return Start building new json array.
	 */
	public static <T> JsonArrayBuilder<?, JsonArray> buildArray(
			Iterable<T> objects, Transform<T> transform) {
		JsonArray a = new JsonArray();
		Impl impl = new Impl(a, a);
		for (T o : objects) {
			impl.add(transform.transform(o));
		}
		return impl;
	}

	public static class Impl<P, R> implements JsonObjectBuilder<P, R>,
			JsonArrayBuilder<P, R> {

		private JsonElement context;
		private JsonElement root;
		private P parent;

		public Impl(JsonElement root, P parent, JsonElement context) {
			this.parent = parent;
			this.root = root;
			this.context = context;
		}

		public Impl(JsonElement root, JsonElement context) {
			this.parent = (P) this;
			this.root = root;
			this.context = context;
		}

		@Override
		public JsonObjectBuilder addObject(String key) {
			JsonObject o = new JsonObject();
			((JsonObject) context).add(key, o);
			return new Impl(root, this, o);
		}

		@Override
		public JsonArrayBuilder addArray(String key) {
			JsonArray o = new JsonArray();
			((JsonObject) context).add(key, o);
			return new Impl(root, this, o);
		}

		@Override
		public JsonArrayBuilder add(Iterable<JsonBuilder> builders) {
			JsonArray array = (JsonArray) context;
			for (JsonBuilder builder : builders) {
				Impl i = ((Impl) builder);
				array.add(i.context);

			}
			return this;
		}

		@Override
		public JsonObjectBuilder add(String key, Iterable<JsonBuilder> builders) {
			JsonArrayBuilder<?, JsonArray> array = JsonBuilderFactory.buildArray();
			for (JsonBuilder b : builders) {
				array.add(b);
			}
			add(key, array);
			
			return this;
		}

		@Override
		public JsonObjectBuilder add(String key, Boolean value) {
			JsonObject obj = (JsonObject) context;
			if (value == null) {
				obj.add(key, JsonNull.INSTANCE);
			} else {
				obj.add(key, new JsonPrimitive(value));
			}
			return this;
		}

		@Override
		public JsonObjectBuilder add(String key, Character value) {
			JsonObject obj = (JsonObject) context;
			if (value == null) {
				obj.add(key, JsonNull.INSTANCE);
			} else {
				obj.add(key, new JsonPrimitive(value));
			}
			return this;
		}

		@Override
		public JsonObjectBuilder add(String key, Number value) {
			JsonObject obj = (JsonObject) context;
			if (value == null) {
				obj.add(key, JsonNull.INSTANCE);
			} else {
				obj.add(key, new JsonPrimitive(value));
			}
			return this;
		}

		@Override
		public JsonObjectBuilder add(String key, String value) {
			JsonObject obj = (JsonObject) context;
			if (value == null) {
				obj.add(key, JsonNull.INSTANCE);
			} else {
				obj.add(key, new JsonPrimitive(value));
			}

			return this;
		}

		@Override
		public JsonArrayBuilder add(JsonBuilder builder) {
			JsonArray array = (JsonArray) context;
			array.add(((Impl) builder).context);
			return this;
		}

		@Override
		public JsonObjectBuilder add(String key, JsonBuilder builder) {
			JsonObject obj = (JsonObject) context;
			obj.add(key, ((Impl) builder).context);
			return this;
		}

		@Override
		public String toString() {
			return root.toString();
		}

		@Override
		public P end() {
			return parent;
		}

		@Override
		public JsonObjectBuilder<JsonArrayBuilder<P, R>, R> addObject() {
			JsonObject o = new JsonObject();
			((JsonArray) context).add(o);
			return new Impl(root, this, o);
		}

		@Override
		public JsonArrayBuilder<JsonArrayBuilder<P, R>, R> addArray() {
			JsonArray o = new JsonArray();
			((JsonArray) context).add(o);
			return new Impl(root, this, o);
		}

		@Override
		public void write(Writer out) throws IOException {
			JsonWriter jsonWriter = new JsonWriter(out);
			Streams.write(root, jsonWriter);
		}

		@Override
		public R getJson() {
			return (R) root;
		}

		@Override
		public JsonArrayBuilder<P, R> add(Boolean value) {
			JsonArray array = ((JsonArray) context);
			if (value == null) {
				array.add(JsonNull.INSTANCE);
			} else {
				array.add(new JsonPrimitive(value));
			}
			return this;
		}

		@Override
		public JsonArrayBuilder<P, R> add(Character value) {
			JsonArray array = ((JsonArray) context);
			if (value == null) {
				array.add(JsonNull.INSTANCE);
			} else {
				array.add(new JsonPrimitive(value));
			}
			return this;
		}

		@Override
		public JsonArrayBuilder<P, R> add(Number value) {
			JsonArray array = ((JsonArray) context);
			if (value == null) {
				array.add(JsonNull.INSTANCE);
			} else {
				array.add(new JsonPrimitive(value));
			}
			return this;
		}

		@Override
		public JsonArrayBuilder<P, R> add(String value) {
			JsonArray array = ((JsonArray) context);
			if (value == null) {
				array.add(JsonNull.INSTANCE);
			} else {
				array.add(new JsonPrimitive(value));
			}
			return this;
		}

		@Override
		public JsonArrayBuilder<P, R> addNull() {
			JsonArray array = ((JsonArray) context);
			array.add(JsonNull.INSTANCE);
			return this;
		}

		@Override
		public JsonObjectBuilder<P, R> addNull(String key) {
			JsonObject obj = ((JsonObject) context);
			obj.add(key, JsonNull.INSTANCE);
			return this;
		}

		@Override
		public void write(JsonWriter out) throws IOException {
			Streams.write(root, out);
		}

		@Override
		public <T> JsonObjectBuilder<P, R> add(String key, Iterable<T> objects,
				Transform<T> transform) {
			JsonArrayBuilder<?, JsonArray> array = JsonBuilderFactory.buildArray();
			for (T o : objects) {
				array.add(transform.transform(o));
			}
			add(key, array);
			return this;
		}

		@Override
		public <T> JsonObjectBuilder<P, R> add(String key, T object,
				Transform<T> transform) {
			add(key, transform.transform(object));
			return this;
		}

		@Override
		public <T> JsonArrayBuilder<P, R> add(Iterable<T> objects,
				Transform<T> transform) {
			JsonArrayBuilder<?, JsonArray> array = JsonBuilderFactory.buildArray();
			for (T o : objects) {
				array.add(transform.transform(o));
			}
			add(array);
			return this;
		}

		@Override
		public <T> JsonArrayBuilder<P, R> add(T object, Transform<T> transform) {
			add(transform.transform(object));
			return this;
		}

	}

}
