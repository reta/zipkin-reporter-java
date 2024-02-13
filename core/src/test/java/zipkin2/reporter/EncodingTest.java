/*
 * Copyright 2016-2024 The OpenZipkin Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package zipkin2.reporter;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EncodingTest {
  @Test void emptyList_json() {
    List<byte[]> encoded = List.of();
    assertThat(Encoding.JSON.encode(encoded))
      .containsExactly('[', ']');
  }

  @Test void singletonList_json() {
    List<byte[]> encoded = List.of(new byte[] {'{', '}'});

    assertThat(Encoding.JSON.encode(encoded))
      .containsExactly('[', '{', '}', ']');
  }

  @Test void multiItemList_json() {
    List<byte[]> encoded = List.of(
      "{\"k\":\"1\"}".getBytes(),
      "{\"k\":\"2\"}".getBytes(),
      "{\"k\":\"3\"}".getBytes()
    );
    assertThat(new String(Encoding.JSON.encode(encoded)))
      .isEqualTo("[{\"k\":\"1\"},{\"k\":\"2\"},{\"k\":\"3\"}]");
  }

  @Test void emptyList_proto3() {
    List<byte[]> encoded = List.of();
    assertThat(Encoding.PROTO3.encode(encoded))
      .isEmpty();
  }

  @Test void singletonList_proto3() {
    List<byte[]> encoded = List.of(new byte[] {1, 1, 'a'});

    assertThat(Encoding.PROTO3.encode(encoded))
      .containsExactly(1, 1, 'a');
  }

  @Test void multiItemList_proto3() {
    List<byte[]> encoded = List.of(
      new byte[] {1, 1, 'a'},
      new byte[] {1, 1, 'b'},
      new byte[] {1, 1, 'c'}
    );
    assertThat(Encoding.PROTO3.encode(encoded)).containsExactly(
      1, 1, 'a',
      1, 1, 'b',
      1, 1, 'c'
    );
  }
}